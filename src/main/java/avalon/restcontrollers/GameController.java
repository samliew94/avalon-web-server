/**
 * 
 *
 * @author Sam Liew 14 Dec 2022 3:45:09 PM
 */
package avalon.restcontrollers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import avalon.entity.GameRoles;
import avalon.entity.RatioRules;
import avalon.entity.Users;

/**
 * @author Sam Liew 14 Dec 2022 3:45:09 PM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/api/game")
@SuppressWarnings({"unchecked", "rawtypes", "serial"})
public class GameController {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private PlayerController playerController;
	
	private int gameId;
	
	@PostMapping("/start/{username}")
	public ResponseEntity startGame(@PathVariable String username)
	{
		try (Session session = sessionFactory.openSession())
		{
			String query = "";
					
			Users user = session.get(Users.class, StringUtils.lowerCase(username));
			
			if (user == null)
				return ResponseEntity.badRequest().body("Username not found");
			
			if (!user.getIsHost())
			{
				query = "FROM Users WHERE isHost = true";
				Users curHost = (Users) session.createNativeQuery(query).getResultStream().findFirst().orElse(null);
				
				if (curHost == null)
					return ResponseEntity.badRequest().body("Only host can start the game but no host found");
				
				return ResponseEntity.badRequest().body("Only host can start the game\nHost = " + curHost.getUsername());
			}
			
			getPlayerController().clearIpUsername();
			
			query = "SELECT u FROM Users u";
			
			List<Users> curPlayers = session.createQuery(query).getResultList();
			
			if (curPlayers.size() < 5) 
				return new ResponseEntity("Insufficient Players. Current is " + curPlayers.size(), HttpStatus.BAD_REQUEST);			
			else if (curPlayers.size() > 10) 
				return new ResponseEntity("Too Many Players! Current is " + curPlayers.size(), HttpStatus.BAD_REQUEST);
						
			RatioRules ratioRules = session.get(RatioRules.class, curPlayers.size());	
			int totalGood = ratioRules.getTotalGood();
			int totalEvil = ratioRules.getTotalEvil();
			
			final Map curPowerPlayers = new HashMap(); // current players with power roles
			
			curPlayers.stream().filter(x->x.getGameRoles() != null && x.getGameRoles().getIsPower() == 1).forEach(x->{
				Object u = x.getUsername();
				Object gr = x.getGameRoles();
				
				curPowerPlayers.put(u, gr);
			});
			
			List<GameRoles> allGameRoles = session.createQuery("FROM GameRoles WHERE isActive = 1").getResultList();
			List<GameRoles> allGamePowerRoles = allGameRoles.stream().filter(x->x.getIsPower() == 1).collect(Collectors.toList());
			GameRoles genG = allGameRoles.stream().filter(x->x.getRoleCode().equalsIgnoreCase("GENG")).findFirst().get();
			GameRoles genE = allGameRoles.stream().filter(x->x.getRoleCode().equalsIgnoreCase("GENE")).findFirst().get();
			
			while(true) // this loop prevents player from getting the same power roles again
			{
				final List<GameRoles> cAllGamePowerRoles = new ArrayList();
				allGamePowerRoles.forEach(x->cAllGamePowerRoles.add(x));
				
				Collections.shuffle(curPlayers);
				
				boolean repeatedPowerRole = false;
				
				for (Users curPlayer : curPlayers) {
					
					if (cAllGamePowerRoles.isEmpty())
						break; 
					
					GameRoles playerGameRole = curPlayer.getGameRoles();
					
					if (playerGameRole == null)
						continue;
					
					Integer gameRolesId = cAllGamePowerRoles.remove(0).getGameRolesId();
					
					if (gameRolesId.equals(playerGameRole.getGameRolesId())) // player gets the same role
					{
						repeatedPowerRole = true;                   
						break;
					}
				}
				
				if (repeatedPowerRole)
					continue;
				
				break; // the first 4 players did not get the same power role
			}
			                         
			for (Users curPlayer : curPlayers) 
			{
				if(!allGamePowerRoles.isEmpty())
				{
					GameRoles gamePowerRole = allGamePowerRoles.remove(0);
					curPlayer.setGameRoles(gamePowerRole);
					
					Integer loyalty = gamePowerRole.getLoyalty();
					
					if (loyalty == 0)
						totalGood -= 1;
					else if (loyalty == 1)
						totalEvil -= 1;
					continue;
				}
				
				if (totalGood > 0)
				{
					totalGood -= 1;
					curPlayer.setGameRoles(genG);
					continue;
				}
				
				if (totalEvil > 0)
				{
					totalEvil -= 1;
					curPlayer.setGameRoles(genE);
					continue;
				}
			}
			
			Transaction transaction = session.beginTransaction();
			
			try {
				
				for (Users curPlayer : curPlayers)
					session.saveOrUpdate(curPlayer);
				
				transaction.commit();
				
				gameId += 1;
				
				return new ResponseEntity(HttpStatus.OK);
				
			} catch (Exception e) {
				transaction.rollback();
				throw e;				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public String getGameId() {
		
		String s = String.valueOf(gameId);
		
		return StringUtils.leftPad(s, 3, "0");
	}
	
	private PlayerController getPlayerController() {
		return applicationContext.getBean(PlayerController.class);
	}
	
}

/** @author Sam Liew 14 Dec 2022 3:45:09 PM */