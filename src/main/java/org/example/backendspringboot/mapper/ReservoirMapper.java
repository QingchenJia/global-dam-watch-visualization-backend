package org.example.backendspringboot.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.backendspringboot.entity.Reservoir;

import java.util.List;

@Mapper
public interface ReservoirMapper extends BaseMapper<Reservoir> {

    @Select("${sql}")
    List<Reservoir> executeSql(@Param("sql") String sql);

    @Select("""
            SELECT 
            ST_AsGeoJSON(wkb_geometry) AS wkb_geometry,res_name,dam_name,alt_name,dam_type,lake_ctrl,river,alt_river,main_basin,sub_basin,country,sec_cntry,admin_unit,sec_admin,near_city,alt_city,year_dam,pre_year,year_src,alt_year,rem_year,timeline,year_txt,dam_hgt_m,alt_hgt_m,dam_len_m,alt_len_m,area_skm,area_poly,area_rep,area_max,area_min,cap_mcm,cap_max,cap_rep,cap_min,depth_m,dis_avg_ls,dor_pc,elev_masl,catch_skm,catch_rep,power_mw,data_info,use_irri,use_elec,use_supp,use_fcon,use_recr,use_navi,use_fish,use_pcon,use_live,use_othr,main_use,multi_dams,comments,url,quality,editor,long_riv,lat_riv,long_dam,lat_dam,orig_src,poly_src,instream,shape_length,shape_area 
            FROM public.gdw_reservoir 
            WHERE gdw_id=#{gdw_id};
            """)
    Reservoir selectByGdwId(@Param("gdw_id") int gdwId);

    @Select("""
            SELECT 
            ${ew.sqlSelect},ST_AsGeoJSON(wkb_geometry) AS wkb_geometry
            FROM public.gdw_reservoir
            ${ew.customSqlSegment}
            """)
    List<Reservoir> selectGeoByCustomCondition(@Param(Constants.WRAPPER) Wrapper<Reservoir> wrapper);
}
