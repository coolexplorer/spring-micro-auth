package io.coolexplorer.auth.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Getter
@Setter
@Accessors(chain = true)
public class Account implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String cellPhone;
}
