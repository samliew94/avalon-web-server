/**
 * 
 *
 * @author Sam Liew 26 Dec 2022 10:36:15 PM
 */
package avalon.restcontrollers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import avalon.entity.GameRoles;

/**
 * @author Sam Liew 26 Dec 2022 10:36:15 PM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/api/roles")
@SuppressWarnings({"unchecked", "rawtypes", "serial"})
public class RolesController {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@GetMapping("/all")
	public ResponseEntity getAllRoles()
	{
		System.out.println("roles/all");
		
		try (Session session = sessionFactory.openSession())
		{
			List<GameRoles> gameRoles = session.createQuery("FROM GameRoles", GameRoles.class).getResultList();
			
			List<Map> results = new ArrayList();
			
			for (GameRoles gameRole : gameRoles) {
				
				Map result = new HashMap();
				result.put("roleId", gameRole.getGameRolesId());
				result.put("roleTitle", gameRole.getRoleTitle());
				result.put("roleTitleColor", gameRole.getRoleTitleColor());
				result.put("roleIcon", gameRole.getRoleIcon());
				result.put("isActive", gameRole.getIsActive());
				result.put("isEditable", gameRole.getIsEditable());
				results.add(result);
			}
			
			return ResponseEntity.ok(results);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/toggle")
	public ResponseEntity toggle(@RequestBody Map body)
	{
		System.out.println("roles/all");
		
		int roleId = (int) body.get("roleId");
		
		try (Session session = sessionFactory.openSession())
		{
			GameRoles gameRole = session.get(GameRoles.class, roleId);
			
			if (gameRole == null)
				return ResponseEntity.badRequest().body("gameRole " + roleId + "not found!");
			
			if (gameRole.getIsEditable() == 0)
				return ResponseEntity.badRequest().body("gameRole " + roleId + "not editable!");
			
			Transaction transaction = session.beginTransaction();
			
			try 
			{
				gameRole.setIsActive(gameRole.getIsActive() == 0 ? 1 : 0);
				
				session.saveOrUpdate(gameRole);
				
				transaction.commit();
			} catch (Exception e) {
				throw e;
			}
			
			return ResponseEntity.ok(null);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}

