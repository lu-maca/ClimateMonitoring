package uni.climatemonitor.data;

import java.util.HashMap;

public class Operator {
    private String name;
    private String taxCode;
    private String email;
    private String username;
    private String password;
    private String monitoringCenter;

    public Operator(HashMap o){
        for (Object key: o.keySet()) {
            switch (key.toString()) {
                case "name":
                    this.name = o.get(key).toString();
                    break;
                case "tax_code":
                    this.taxCode = o.get(key).toString();
                    break;
                case "email":
                    this.email = o.get(key).toString();
                    break;
                case "username":
                    this.username = o.get(key).toString();
                    break;
                case "password":
                    this.password = o.get(key).toString();
                    break;
                case "monitoring_center":
                    this.monitoringCenter = o.get(key).toString();
                    break;
            }
        }
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
}
