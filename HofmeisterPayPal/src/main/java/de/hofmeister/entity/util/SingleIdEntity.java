package de.hofmeister.entity.util;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SingleIdEntity<PK> implements Serializable {
    public abstract PK getId();

    @Override
    public boolean equals(Object other) {
        if(other instanceof SingleIdEntity)
            return Objects.equals(this.getId(), ((SingleIdEntity)other).getId());
        else
            return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }
}
