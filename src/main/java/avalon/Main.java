/**
 * 
 *
 * @author Sam Liew 14 Dec 2022 3:30:49 PM
 */
package avalon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;



/**
 * @author Sam Liew 14 Dec 2022 3:30:49 PM
 *
 */
@SpringBootApplication
public class Main {
	
	public static void main(String[] args) {
        try {
			SpringApplication.run(Main.class, args);
			System.out.println(SpringVersion.getVersion());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//        System.exit(0);
			
		}
    }
	
}