package net.ldauvilaire.sample.batch.domain.model;


import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CCVVIsibility extends Audit {

    private String id;

    private String material;

    private String land;

    private String charName;

    private String charValue;

    private Date eow;

    private Date announce;

    private Date withdraw;

    private String active;

    private String internalAudienc;

    private String leAud;

    private String bpAud;

    private String pubAud;

    private String objKey;

}
