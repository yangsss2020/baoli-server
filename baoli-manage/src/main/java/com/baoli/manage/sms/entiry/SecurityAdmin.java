package com.baoli.sms.entiry;

import com.baoli.sms.entity.Admin;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author ys
 * @create 2020-02-11-11:17
 */
@Getter
public class SecurityAdmin extends User {
    private Admin originalAdmin;
    public SecurityAdmin(Admin originalAdmin, Collection<? extends GrantedAuthority> authorities) {
        super(originalAdmin.getUsername(), originalAdmin.getPassword(), authorities);
        this.originalAdmin = originalAdmin;
    }

}
