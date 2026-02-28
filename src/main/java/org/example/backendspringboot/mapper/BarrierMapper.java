package org.example.backendspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.backendspringboot.common.StatsData;
import org.example.backendspringboot.entity.Barrier;

import java.util.List;

@Mapper
public interface BarrierMapper extends BaseMapper<Barrier> {

    @Select("${sql}")
    List<Barrier> executeSql(@Param("sql") String sql);

    @Select("""
            SELECT gdw_id,ST_AsGeoJSON(wkb_geometry) AS wkb_geometry,dam_type,dam_name FROM public.gdw_barrier;
            """)
    List<Barrier> selectAllLocation();

    @Select("""
            SELECT ogc_fid,ST_AsGeoJSON(wkb_geometry) AS wkb_geometry,objectid,gdw_id,res_name,dam_name,alt_name,dam_type,lake_ctrl,river,alt_river,main_basin,sub_basin,country,sec_cntry,admin_unit,sec_admin,near_city,alt_city,year_dam,pre_year,year_src,alt_year,rem_year,timeline,year_txt,dam_hgt_m,alt_hgt_m,dam_len_m,alt_len_m,area_skm,area_poly,area_rep,area_max,area_min,cap_mcm,cap_max,cap_rep,cap_min,depth_m,dis_avg_ls,dor_pc,elev_masl,catch_skm,catch_rep,power_mw,data_info,use_irri,use_elec,use_supp,use_fcon,use_recr,use_navi,use_fish,use_pcon,use_live,use_othr,main_use,multi_dams,comments,url,quality,editor,long_riv,lat_riv,long_dam,lat_dam,orig_src,poly_src,grand_id,hyriv_id,instream,hylak_id,hybas_l12 FROM public.gdw_barrier WHERE gdw_id=#{gdw_id};
            """)
    Barrier selectByGdwId(@Param("gdw_id") int gdwId);

    @Select("""
            SELECT gdw_id,dam_name FROM public.gdw_barrier WHERE dam_name LIKE #{damName};
            """)
    List<Barrier> selectLikeName(String damName);

    @Select("""
            SELECT dam_type as name,COUNT(*) as value FROM public.gdw_barrier GROUP BY dam_type;
            """)
    List<StatsData> damTypeStats();

    @Select("""
            SELECT power_mw as name,COUNT(*) as value FROM public.gdw_barrier GROUP BY power_mw;
            """)
    List<StatsData> powerMwStats();

    @Select("""
            SELECT main_use as name,COUNT(*) as value FROM public.gdw_barrier GROUP BY main_use;
            """)
    List<StatsData> mainUseStats();

    @Select("""
            SELECT quality as name,COUNT(*) as value FROM public.gdw_barrier GROUP BY quality;
            """)
    List<StatsData> qualityStats();

    @Select("""
            SELECT year_dam as name,COUNT(*) as value FROM public.gdw_barrier GROUP BY year_dam;
            """)
    List<StatsData> yearDamStats();

    @Select("""
            SELECT country as name,COUNT(*) as value FROM public.gdw_barrier GROUP BY country;
            """)
    List<StatsData> countryStats();
}
