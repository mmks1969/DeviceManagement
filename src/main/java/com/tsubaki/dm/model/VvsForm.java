package com.tsubaki.dm.model;


import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class VvsForm {

    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private int vvsId; // vvsID

    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private String usedAttribute; // 使用項目

    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private String sortKey; // 並び順
    
    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private String key; // キー
    
    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private String value; // 値
    
}
