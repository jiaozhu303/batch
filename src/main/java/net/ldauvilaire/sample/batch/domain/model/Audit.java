package net.ldauvilaire.sample.batch.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public abstract class Audit {
    private String creator = "";

    private Date createDate;

    private String changer = "";

    private Date changeDate;

    public Audit(String creator, Date createDate, String changer, Date changeDate) {
        this.changer = changer;
        this.changeDate = changeDate;
        this.creator = creator;
        this.createDate = createDate;
    }
}
