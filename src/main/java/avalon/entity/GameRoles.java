/**
 * 
 *
 * @author Sam Liew 14 Dec 2022 4:06:46 PM
 */
package avalon.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Sam Liew 14 Dec 2022 4:06:46 PM
 *
 */
@Entity
@Data
public class GameRoles {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameRolesId;
	private String roleCode;
	private String roleTitle;
	private String roleTitleColor;
	private String roleIcon;
	private String roleDescription;
	private String otherPlayersTitle;
	private String otherPlayersNameColor;
	
	private Integer loyalty; 
	private Integer isPower;
	private Integer isActive;
	private Integer isEditable;
	
	
}

/** @author Sam Liew 14 Dec 2022 4:06:46 PM */