/*************************************************
 * Operator class
 * @author Luca Macavero, 755091, lmacavero@studenti.uninsubria.it, VA
 *
 */


package uni.climatemonitor.data;

import java.util.HashMap;

public class Operator {
    private String name;
    private String taxCode;
    private String email;
    private String username;
    private String password;
    private String monitoringCenter;
    private String jsonFormat =
            """
  {
      "name": "%s",
      "tax_code": "%s",
      "email": "%s",
      "username": "%s",
      "password": "%s",
      "monitoring_center": "%s"
    }""";


    public Operator(HashMap o){
        this.name = o.get("name").toString();
        this.taxCode = o.get("tax_code").toString();
        this.email = o.get("email").toString();
        this.username = o.get("username").toString();
        this.password = o.get("password").toString();
        this.monitoringCenter = o.get("monitoring_center").toString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMonitoringCenter() {
        return monitoringCenter;
    }

    public String getPassword() {
        return password;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString(){ return getUsername(); }

    public String toJson(){
        String out = String.format(
                jsonFormat,
                name,
                taxCode,
                email,
                username,
                password,
                monitoringCenter
        );
        return out;
    }
}
