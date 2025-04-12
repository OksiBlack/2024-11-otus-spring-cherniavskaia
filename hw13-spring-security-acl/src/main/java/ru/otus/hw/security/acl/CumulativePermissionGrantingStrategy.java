package ru.otus.hw.security.acl;

import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;

public class CumulativePermissionGrantingStrategy extends DefaultPermissionGrantingStrategy {
    /**
     * Creates an instance with the logger which will be used to record granting and
     * denial of requested permissions.
     */
    public CumulativePermissionGrantingStrategy(AuditLogger auditLogger) {
        super(auditLogger);
    }

    @Override
    protected boolean isGranted(AccessControlEntry ace, Permission p) {
        // If the ACE is granting and the requested permission has a non-zero mask,
        // perform a bitwise comparison to see if any bits match.
        if (ace.isGranting() && p.getMask() != 0) {
            return (ace.getPermission().getMask() & p.getMask()) != 0;
        } else {
            // Otherwise, check for exact match.
            return ace.getPermission().getMask() == p.getMask();
        }
    }
}
