/**
 * 
 *
 * @author Sam Liew 14 Dec 2022 3:45:09 PM
 */
package avalon.restcontrollers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import avalon.entity.GameRoles;
import avalon.entity.Users;
import avalon.utils.StringUtil;

/**
 * @author Sam Liew 14 Dec 2022 3:45:09 PM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/api/players")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PlayerController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ApplicationContext applicationContext;;

	private Map<String, String> ipUsername = new HashMap();

	@PostMapping("/add/{username}")
	public synchronized ResponseEntity addPlayer(@PathVariable String username) {
		
		System.out.println("add/"+username);
		
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();

			try {
				Users user = session.get(Users.class, username.toLowerCase());

				if (user != null)
					return new ResponseEntity(user, HttpStatus.OK);

				user = new Users();
				user.setUsername(username);

				int totalPlayers = session.createQuery("FROM Users").getResultList().size();

				if (totalPlayers == 0)
					user.setIsHost(true);

				session.saveOrUpdate(user);

				transaction.commit();

				return new ResponseEntity(user, HttpStatus.OK);
			} catch (Exception e) {
				// TODO: handle exception
				transaction.rollback();
				throw e;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/del/{fromUsername}/{targetUsername}")
	public ResponseEntity delPlayer(@PathVariable String fromUsername, @PathVariable String targetUsername) {
		if (fromUsername.equalsIgnoreCase(targetUsername))
			return ResponseEntity.badRequest().body("Can't delete yourself");

		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();

			try {
				Users user = session.get(Users.class, targetUsername);

				if (user == null)
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);

				session.remove(user);

				transaction.commit();

				return new ResponseEntity(HttpStatus.OK);
			} catch (Exception e) {
				// TODO: handle exception
				transaction.rollback();
				throw e;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("all/{username}")
	public ResponseEntity getAllPlayers(@PathVariable final String username) {

		System.out.println("all/" + username);

		try (Session session = sessionFactory.openSession()) 
		{
//			System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(seeAllRole())); // debug only
			
			String query = "FROM Users";

			List<Users> users = session.createQuery(query).getResultList();

			Map result = new HashMap();
			result.put("isHost", false);

			List players = new ArrayList();

			for (Users user : users) {
				players.add(user.getUsername());

				if (user.getUsername().equalsIgnoreCase(username) && user.getIsHost() != null && user.getIsHost())
					result.put("isHost", true);
			}

			Collections.sort(players); // sort username asc

			result.put("players", players);

			return new ResponseEntity(result, HttpStatus.OK); // {"isHost":true, "players": [...]}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public Map seeAllRole() throws Exception
	{
		try (Session session = sessionFactory.openSession()) {
			String query = "SELECT u.username, gr.role_title " + "FROM Users u "
					+ "LEFT JOIN game_roles gr on gr.game_roles_id = u.game_roles_id";

			List<Object[]> t = session.createNativeQuery(query).getResultList();

			Map results = new HashMap();

			for (int i = 0; i < t.size(); i++) {
				Object[] x = t.get(i);
				String username = (String) x[0];
				String roleName = (String) x[1];
				results.put(username, roleName);
			}

			return results;

		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		
	}

	@GetMapping("seerole/{username}")
	public ResponseEntity seerole(@PathVariable String username, HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		System.out.println(ip + " seerole/" + username);

		try (Session session = sessionFactory.openSession()) {
			username = username.toLowerCase();

			Users user = session.get(Users.class, username.toLowerCase());

			if (user == null)
				return new ResponseEntity(username.toString() + " not found!", HttpStatus.NOT_FOUND);

			if (user.getGameRoles() == null)
				return new ResponseEntity("Secret Role unassigned\nGame not started", HttpStatus.NOT_FOUND);

			if (ipUsername.containsKey(ip) && !ipUsername.get(ip).equalsIgnoreCase(username))
				return new ResponseEntity("You've already seen your role.\nDon't Cheat!", HttpStatus.BAD_REQUEST);

			ipUsername.put(ip, username);

			Map result = new HashMap();

			GameRoles userGameRoles = user.getGameRoles();

			result.put("gameId", getGameController().getGameId());
			result.put("playerName", user.getUsername());
			result.put("loyalty", userGameRoles.getLoyalty());
			result.put("roleTitle", userGameRoles.getRoleTitle());
			result.put("roleTitleColor", userGameRoles.getRoleTitleColor());
			result.put("roleDescription", userGameRoles.getRoleDescription());
			result.put("otherPlayersTitle", userGameRoles.getOtherPlayersTitle());
			result.put("otherPlayersNameColor", userGameRoles.getOtherPlayersNameColor());
			result.put("roleIcon", userGameRoles.getRoleIcon());

			String query = "SELECT grv.canSeeGameRoles.gameRolesId FROM GameRolesVisibility grv WHERE grv.gameRoles.gameRolesId = :gameRolesId";

			List otherPlayersGameRolesId = session.createQuery(query)
					.setParameter("gameRolesId", user.getGameRoles().getGameRolesId()).getResultList();

			if (otherPlayersGameRolesId.isEmpty())
				result.put("otherPlayersName", new ArrayList());
			else {
				query = "SELECT username FROM Users WHERE gameRoles.gameRolesId IN (:gameRolesId) AND username != :username";

				List otherPlayers = session.createQuery(query).setParameterList("gameRolesId", otherPlayersGameRolesId)
						.setParameter("username", username).getResultList();

				result.put("otherPlayersName", otherPlayers);
			}

			return new ResponseEntity(result, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("makehost/{username}/{targetUsername}")
	public ResponseEntity makeHost(@PathVariable String username, @PathVariable String targetUsername) {
		try (Session session = sessionFactory.openSession()) {
			if (username.equalsIgnoreCase(targetUsername))
				return ResponseEntity.badRequest().body("You are already the host!");

			Users user = session.get(Users.class, StringUtils.lowerCase(username));

			if (user == null)
				return ResponseEntity.badRequest().body("Can't find my username '" + username + "'");

			if (!user.getIsHost())
				return ResponseEntity.badRequest().body("Only host can assign another player as host");

			Users targetUser = session.get(Users.class, targetUsername);

			if (targetUser == null)
				return ResponseEntity.badRequest().body("Can't find target username");

			Transaction transaction = session.beginTransaction();

			try {
				String query = "FROM Users";

				List<Users> users = session.createQuery(query, Users.class).getResultList();

				for (Users u : users) {
					u.setIsHost(u.getUsername().equalsIgnoreCase(targetUsername));
					session.saveOrUpdate(u);
				}

				transaction.commit();

				return ResponseEntity.ok("New host set to " + StringUtil.capitalize(targetUsername));

			} catch (Exception e) {
				throw e;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public void clearIpUsername() {
		ipUsername.clear();
	}

	private GameController getGameController() {
		return applicationContext.getBean(GameController.class);
	}

}