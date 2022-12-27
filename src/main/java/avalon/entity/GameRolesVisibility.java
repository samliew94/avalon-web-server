/**
 * 
 *
 * @author Sam Liew 15 Dec 2022 2:44:50 PM
 */
package avalon.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 * @author Sam Liew 15 Dec 2022 2:44:50 PM
 *
 */
@Entity
@Data
public class GameRolesVisibility {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer gameRolesVisibilityId;
	
	@ManyToOne
	@JoinColumn(name = "game_roles_id")
	public GameRoles gameRoles;
	
	@ManyToOne
	@JoinColumn(name = "can_see_game_roles_id")
	public GameRoles canSeeGameRoles;
	
}

