package org.example.backendspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("public.gdw_reservoir")
public class Reservoir implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer ogcFid;

    private String wkbGeometry;

    private Long objectid;

    private Long gdwId;

    private String resName;

    private String damName;

    private String altName;

    private String damType;

    private String lakeCtrl;

    private String river;

    private String altRiver;

    private String mainBasin;

    private String subBasin;

    private String country;

    private String secCntry;

    private String adminUnit;

    private String secAdmin;

    private String nearCity;

    private String altCity;

    private Long yearDam;

    private Long preYear;

    private String yearSrc;

    private Long altYear;

    private Long remYear;

    private String timeline;

    private String yearTxt;

    private Long damHgtM;

    private Long altHgtM;

    private Long damLenM;

    private Long altLenM;

    private Double areaSkm;

    private Double areaPoly;

    private Double areaRep;

    private Double areaMax;

    private Double areaMin;

    private Double capMcm;

    private Double capMax;

    private Double capRep;

    private Double capMin;

    private Double depthM;

    private Long disAvgLs;

    private Double dorPc;

    private Long elevMasl;

    private Long catchSkm;

    private Long catchRep;

    private Double powerMw;

    private String dataInfo;

    private String useIrri;

    private String useElec;

    private String useSupp;

    private String useFcon;

    private String useRecr;

    private String useNavi;

    private String useFish;

    private String usePcon;

    private String useLive;

    private String useOthr;

    private String mainUse;

    private String multiDams;

    private String comments;

    private String url;

    private String quality;

    private String editor;

    private Double longRiv;

    private Double latRiv;

    private Double longDam;

    private Double latDam;

    private String origSrc;

    private String polySrc;

    private Long grandId;

    private Long hyrivId;

    private String instream;

    private Long hylakId;

    private Double hybasL12;

    private Double shapeLength;

    private Double shapeArea;
}
