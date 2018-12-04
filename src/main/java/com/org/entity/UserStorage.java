package com.org.entity;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import com.org.domain.LogUser;
import com.org.web.ManagedDocumentController;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findUserStoragesByLogUser" })
public class UserStorage {
	final static Logger logger = Logger.getLogger(UserStorage.class);
	
    private long storageLimit;
    
    public static long DEFAULT_STORAGE_LIMIT=200;

    @OneToOne
    private LogUser logUser;
    
    public static long getStorageLimitByUser(LogUser user) {
    	try {
    		UserStorage result = UserStorage.findUserStoragesByLogUser(user).getSingleResult();
    		return result.getStorageLimit();
    		
		} catch (Exception e) {
			logger.info("no storage set for user ");
			System.out.println("no storage set for user ");
			return DEFAULT_STORAGE_LIMIT;
		}
    	
    }
}
