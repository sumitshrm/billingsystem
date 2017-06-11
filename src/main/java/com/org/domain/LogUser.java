package com.org.domain;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FlushModeType;
import javax.persistence.ManyToMany;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.context.SecurityContextHolder;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findLogUsersByUsernameEquals" })
public class LogUser {

    @NotNull
    private Boolean enabled;

    @NotNull
    @Size(max = 20, min = 4)
    @Column(unique = true)
    private String username;

    @NotNull
    @Size(max = 10, min = 4)
    private String password;

    @NotNull
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<LogUserRole> roles;
    
    public static LogUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TypedQuery<LogUser> query = LogUser.findLogUsersByUsernameEquals(username);
        query.setFlushMode(FlushModeType.COMMIT);
        LogUser user = query.getSingleResult();
        return user;
    }
}
