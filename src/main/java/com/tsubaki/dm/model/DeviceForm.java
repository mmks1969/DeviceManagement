package com.tsubaki.dm.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DeviceForm {

    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private String deviceId; // デバイスID

    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private String kataban; // 形番

    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private String owner; // オーナー
    
    //必須入力
    @NotBlank(groups = ValidGroup1.class, message = "{require_check}")
    private String maker; // メーカー
    
    //必須入力
    @NotNull(groups = ValidGroup2.class, message = "{require_check}")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private String purchaseDate; // 購入日
    
    //必須入力
    @NotBlank(groups = ValidGroup2.class, message = "{require_check}")
    private String deviceKbn; // デバイス区分
    
    private List<MultipartFile> file; // 添付ファイル(登録)

//    private List<FileBean> tempufiles; // 添付ファイル

    private String creationdate; // 作成日

    private String lastupdate; // 更新日
    
}
