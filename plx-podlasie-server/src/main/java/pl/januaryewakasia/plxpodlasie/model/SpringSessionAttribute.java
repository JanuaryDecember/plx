package pl.januaryewakasia.plxpodlasie.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "SPRING_SESSION_ATTRIBUTES")
public class SpringSessionAttribute implements Serializable {

    @EmbeddedId
    private SpringSessionAttributesId id;

    @Lob
    @Column(name = "ATTRIBUTE_BYTES", nullable = false, columnDefinition = "bytea")
    private byte[] attributeBytes;

    public SpringSessionAttributesId getId() {
        return id;
    }

    public void setId(SpringSessionAttributesId id) {
        this.id = id;
    }

    public byte[] getAttributeBytes() {
        return attributeBytes;
    }

    public void setAttributeBytes(byte[] attributeBytes) {
        this.attributeBytes = attributeBytes;
    }
}
