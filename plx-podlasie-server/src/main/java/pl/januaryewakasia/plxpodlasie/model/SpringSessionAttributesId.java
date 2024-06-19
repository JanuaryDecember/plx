package pl.januaryewakasia.plxpodlasie.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class SpringSessionAttributesId implements Serializable {

    @Column(name = "SESSION_PRIMARY_ID", nullable = false)
    private String sessionPrimaryId;

    @Column(name = "ATTRIBUTE_NAME", nullable = false)
    private String attributeName;

    public String getSessionPrimaryId() {
        return sessionPrimaryId;
    }

    public void setSessionPrimaryId(String sessionPrimaryId) {
        this.sessionPrimaryId = sessionPrimaryId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}

