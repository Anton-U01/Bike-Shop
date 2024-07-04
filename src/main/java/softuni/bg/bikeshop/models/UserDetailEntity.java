package softuni.bg.bikeshop.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

public class UserDetailEntity extends User {
    private String fullName;
    private int age;
    private String email;


    public UserDetailEntity(String fullName,int age,String email,String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.fullName = fullName;
        this.age = age;
        this.email = email;
    }

    public UserDetailEntity(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
