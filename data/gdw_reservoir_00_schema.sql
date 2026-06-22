/*
 Source Server Type    : PostgreSQL
 Source Server Version : 140018 (140018)
 Source Catalog        : gdw
 Source Schema         : public
*/


-- ----------------------------
-- Table structure for gdw_reservoir
-- ----------------------------
DROP TABLE IF EXISTS "public"."gdw_reservoir";
CREATE TABLE "public"."gdw_reservoir" (
  "ogc_fid" int4 NOT NULL DEFAULT nextval('gdw_reservoir_ogc_fid_seq'::regclass),
  "wkb_geometry" geometry(MULTIPOLYGON, 4326),
  "objectid" numeric(20,0),
  "gdw_id" numeric(10,0),
  "res_name" varchar(50) COLLATE "pg_catalog"."default",
  "dam_name" varchar(50) COLLATE "pg_catalog"."default",
  "alt_name" varchar(50) COLLATE "pg_catalog"."default",
  "dam_type" varchar(20) COLLATE "pg_catalog"."default",
  "lake_ctrl" varchar(10) COLLATE "pg_catalog"."default",
  "river" varchar(50) COLLATE "pg_catalog"."default",
  "alt_river" varchar(50) COLLATE "pg_catalog"."default",
  "main_basin" varchar(50) COLLATE "pg_catalog"."default",
  "sub_basin" varchar(50) COLLATE "pg_catalog"."default",
  "country" varchar(50) COLLATE "pg_catalog"."default",
  "sec_cntry" varchar(50) COLLATE "pg_catalog"."default",
  "admin_unit" varchar(50) COLLATE "pg_catalog"."default",
  "sec_admin" varchar(50) COLLATE "pg_catalog"."default",
  "near_city" varchar(50) COLLATE "pg_catalog"."default",
  "alt_city" varchar(50) COLLATE "pg_catalog"."default",
  "year_dam" numeric(10,0),
  "pre_year" numeric(10,0),
  "year_src" varchar(10) COLLATE "pg_catalog"."default",
  "alt_year" numeric(10,0),
  "rem_year" numeric(10,0),
  "timeline" varchar(25) COLLATE "pg_catalog"."default",
  "year_txt" varchar(30) COLLATE "pg_catalog"."default",
  "dam_hgt_m" numeric(10,0),
  "alt_hgt_m" numeric(10,0),
  "dam_len_m" numeric(10,0),
  "alt_len_m" numeric(10,0),
  "area_skm" float8,
  "area_poly" float8,
  "area_rep" float8,
  "area_max" float8,
  "area_min" float8,
  "cap_mcm" float8,
  "cap_max" float8,
  "cap_rep" float8,
  "cap_min" float8,
  "depth_m" float8,
  "dis_avg_ls" numeric(10,0),
  "dor_pc" float8,
  "elev_masl" numeric(10,0),
  "catch_skm" numeric(10,0),
  "catch_rep" numeric(10,0),
  "power_mw" float8,
  "data_info" varchar(50) COLLATE "pg_catalog"."default",
  "use_irri" varchar(5) COLLATE "pg_catalog"."default",
  "use_elec" varchar(5) COLLATE "pg_catalog"."default",
  "use_supp" varchar(5) COLLATE "pg_catalog"."default",
  "use_fcon" varchar(5) COLLATE "pg_catalog"."default",
  "use_recr" varchar(5) COLLATE "pg_catalog"."default",
  "use_navi" varchar(5) COLLATE "pg_catalog"."default",
  "use_fish" varchar(5) COLLATE "pg_catalog"."default",
  "use_pcon" varchar(5) COLLATE "pg_catalog"."default",
  "use_live" varchar(5) COLLATE "pg_catalog"."default",
  "use_othr" varchar(5) COLLATE "pg_catalog"."default",
  "main_use" varchar(25) COLLATE "pg_catalog"."default",
  "multi_dams" varchar(10) COLLATE "pg_catalog"."default",
  "comments" varchar(250) COLLATE "pg_catalog"."default",
  "url" varchar(150) COLLATE "pg_catalog"."default",
  "quality" varchar(25) COLLATE "pg_catalog"."default",
  "editor" varchar(10) COLLATE "pg_catalog"."default",
  "long_riv" float8,
  "lat_riv" float8,
  "long_dam" float8,
  "lat_dam" float8,
  "orig_src" varchar(10) COLLATE "pg_catalog"."default",
  "poly_src" varchar(15) COLLATE "pg_catalog"."default",
  "grand_id" numeric(10,0),
  "hyriv_id" numeric(10,0),
  "instream" varchar(10) COLLATE "pg_catalog"."default",
  "hylak_id" numeric(10,0),
  "hybas_l12" float8,
  "shape_length" float8,
  "shape_area" float8
)
;

-- ----------------------------
-- Records of gdw_reservoir
-- ----------------------------
