package com.example.nobukuni2023.form;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ReviewRegisterForm {

    @NotNull
    private Integer houseid;

    @OneToMany
    @NotNull
    private Integer userid;

    @NotBlank
    private String commenttext;

    @NotNull
    private Integer value;

    public ReviewRegisterForm(Integer houseid, Integer userid, String commenttext, Integer value) {
        this.houseid = houseid;
        this.userid = userid;
        this.commenttext = commenttext;
        this.value = value;
    }
}