/**
 * 
 *
 * @author Sam Liew 14 Dec 2022 8:02:04 PM
 */
package avalon.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * @author Sam Liew 14 Dec 2022 8:02:04 PM
 *
 */
@Entity
@Data
public class Users {
	
	@Id
	private String username;
	
	@ManyToOne
	@JoinColumn(name = "game_roles_id")
	private GameRoles gameRoles;
	
	private Boolean isHost;
}

