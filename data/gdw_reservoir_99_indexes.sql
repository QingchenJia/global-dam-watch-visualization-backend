
-- ----------------------------
-- Indexes structure for table gdw_reservoir
-- ----------------------------
CREATE INDEX "gdw_reservoir_wkb_geometry_geom_idx" ON "public"."gdw_reservoir" USING gist (
  "wkb_geometry" "public"."gist_geometry_ops_2d"
);

-- ----------------------------
-- Primary Key structure for table gdw_reservoir
-- ----------------------------
ALTER TABLE "public"."gdw_reservoir" ADD CONSTRAINT "gdw_reservoir_pk" PRIMARY KEY ("ogc_fid");
