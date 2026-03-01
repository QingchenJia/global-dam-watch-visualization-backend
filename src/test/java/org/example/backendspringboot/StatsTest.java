package org.example.backendspringboot;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.backendspringboot.common.RangeData;
import org.example.backendspringboot.common.StatsData;
import org.example.backendspringboot.dto.BarrierForm;
import org.example.backendspringboot.entity.Barrier;
import org.example.backendspringboot.service.BarrierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class StatsTest {

    @Autowired
    private BarrierService barrierService;

    @Test
    void barrierStats() {
        LambdaQueryWrapper<Barrier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Barrier::getDamType,
                Barrier::getDamLenM,
                Barrier::getMainUse,
                Barrier::getQuality,
                Barrier::getYearDam,
                Barrier::getCountry
        );
        List<Barrier> baseBarriers = barrierService.list(queryWrapper);

        Map<String, Integer> countByDamType = baseBarriers.stream()
                .collect(Collectors.groupingBy(
                        Barrier::getDamType,
                        Collectors.summingInt(e -> 1)
                ));

        System.out.println("dam_type:");
        countByDamType.forEach((key, value) -> System.out.println(key + "\t" + value));

        Map<Double, Integer> countByPowerMw = baseBarriers.stream()
                .collect(Collectors.groupingBy(
                        Barrier::getPowerMw,
                        Collectors.summingInt(e -> 1)
                ));

        System.out.println("power_mw:");
        countByPowerMw.remove(NumberUtils.toDouble("-99"));
        countByPowerMw.forEach((key, value) -> System.out.println(key + "\t" + value));

        Map<String, Integer> countByMainUse = baseBarriers.stream()
                .collect(Collectors.groupingBy(
                        Barrier::getMainUse,
                        Collectors.summingInt(e -> 1)
                ));

        System.out.println("main_use:");
        countByMainUse.remove("");
        countByMainUse.forEach((key, value) -> System.out.println(key + "\t" + value));

        Map<String, Integer> countByQuality = baseBarriers.stream()
                .collect(Collectors.groupingBy(
                        Barrier::getQuality,
                        Collectors.summingInt(e -> 1)
                ));

        System.out.println("quality:");
        countByQuality.forEach((key, value) -> System.out.println(key + "\t" + value));

        Map<Long, Integer> countByYearDam = baseBarriers.stream()
                .collect(Collectors.groupingBy(
                        Barrier::getYearDam,
                        Collectors.summingInt(e -> 1)
                ));

        System.out.println("year_dam:");
        countByYearDam.remove(NumberUtils.toLong("-99"));
        countByYearDam.forEach((key, value) -> System.out.println(key + "\t" + value));

        Map<String, Integer> countByCountry = baseBarriers.stream()
                .collect(Collectors.groupingBy(
                        Barrier::getCountry,
                        Collectors.summingInt(e -> 1)
                ));

        System.out.println("country:");
        countByCountry.forEach((key, value) -> System.out.println(key + "\t" + value));

        List<StatsData> damType = countByDamType.entrySet()
                .stream()
                .map(stringIntegerEntry ->
                        new StatsData(stringIntegerEntry.getKey(), stringIntegerEntry.getValue()))
                .toList();

        damType.forEach(System.out::println);
    }

    @Test
    void filterWithCondition() {
        LambdaQueryWrapper<Barrier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Barrier::getDamType,
                Barrier::getDamLenM,
                Barrier::getMainUse,
                Barrier::getQuality,
                Barrier::getYearDam,
                Barrier::getCountry
        );
        BarrierForm form = new BarrierForm(
                List.of("Dam"),
                new RangeData(-1, -1, -1, -1, 100),
                List.of("Betsiboka",
                        "Mangamahoe",
                        "Waihopai",
                        "Shamil and Zendan",
                        "Amazon",
                        "Yenice",
                        "Red",
                        "Li Shui",
                        "Sittaung",
                        "Tabasara",
                        "Waitakere",
                        "Jiulong",
                        "Kanagarayan Aru",
                        "Thu Bon",
                        "Rufiji",
                        "La Grande",
                        "Amur",
                        "Loukkos",
                        "Sebou",
                        "Hari",
                        "Lake Balkhash",
                        "Lo",
                        "Han",
                        "Saint Lawrence",
                        "Pampanga",
                        "Naiwhakangoro",
                        "Yalong",
                        "Yamuna",
                        "Sao Francisco River",
                        "Clutha",
                        "Superior",
                        "Yellow",
                        "Ningyuan",
                        "Amazonas",
                        "Ou",
                        "Volga",
                        "Savannah",
                        "Helleh",
                        "Itajai",
                        "Burnett River",
                        "Moulouya",
                        "Tennessee",
                        "Irtysh",
                        "Con",
                        "Hutt",
                        "Huong",
                        "Wainuiomata",
                        "Euphrates",
                        "St. Lawrence",
                        "Da",
                        "Mindanao",
                        "North Interior",
                        "Huai He",
                        "Rio Grande do Sul",
                        "Baixi",
                        "Zoreh",
                        "Kitakami River",
                        "Konkoure",
                        "Sungai Tutong",
                        "Draa",
                        "South Interior",
                        "Lake Uluabat",
                        "Dez",
                        "Mangapapa",
                        "West Coast",
                        "Rabigh",
                        "James Bay",
                        "Polrood",
                        "Agano",
                        "Congo",
                        "Bosten Lake",
                        "Santiago",
                        "Ganges",
                        "Daling",
                        "Coruh",
                        "Jacui-Camaqua-Jaguaron",
                        "Limpopo",
                        "Atlantico Leste",
                        "Oued El Kebir",
                        "Irrawady",
                        "Brahmani",
                        "Hays",
                        "Qing Jiang",
                        "Longjiang",
                        "Changuinola",
                        "Guadalquivir",
                        "Tarim",
                        "Columbia",
                        "Uruguay",
                        "Oued El Kabir",
                        "Shatt al Arab",
                        "Ebro",
                        "North West Coast",
                        "Paraita",
                        "Saint Lawerence",
                        "Min Jiang",
                        "Manuk",
                        "Ou Jiang",
                        "Biobio",
                        "Mangatangi",
                        "Kura",
                        "Bang Pakong",
                        "Sao Francisco",
                        "Saint John",
                        "Sinu",
                        "Ili",
                        "Perak",
                        "Guapo",
                        "Karaj",
                        "Shebelle-Jubba",
                        "Susquehanna",
                        "Futaleufu",
                        "Han Shui",
                        "Ba",
                        "Mogami",
                        "South Atlantic Coast",
                        "Hai He",
                        "Periyar",
                        "Aliakmonas",
                        "Maule-Itata",
                        "Wadi Baysh",
                        "Save",
                        "Xun Jiang",
                        "Tamjin",
                        "Grijalva-Usumacinta",
                        "Lampung",
                        "Zi Shui",
                        "Camana",
                        "Abukuma",
                        "Hamzadere",
                        "Xin Jiang",
                        "Connecticut",
                        "Chang Jiang",
                        "Yalu",
                        "Kucukmenderes",
                        "Huia",
                        "Seongju",
                        "Paraiba do Sul",
                        "Seyhan",
                        "Seman",
                        "Dawa",
                        "Guadiana",
                        "Muda",
                        "Brahmaputra",
                        "Fuchun",
                        "Douro",
                        "Karkheh",
                        "Waiarohia",
                        "Rift Valley",
                        "Kama",
                        "Tuo Jiang",
                        "Yangtze",
                        "Dasht",
                        "Desaguadero River",
                        "Nihotupu",
                        "Mekong",
                        "Melet",
                        "Bio-Bio",
                        "Cauvery",
                        "Papaloapan",
                        "Tra Khuc",
                        "Mond",
                        "Ma",
                        "Ca",
                        "Melen",
                        "Dim",
                        "Presidio",
                        "Murray",
                        "Hali",
                        "Black Volta",
                        "Mako",
                        "Rio Grande de Matagalpa",
                        "Bengawan",
                        "Krishna-Godavari",
                        "Taylor",
                        "Jordan",
                        "Yaque del Norte River",
                        "Terme",
                        "Karnafuli",
                        "Qom",
                        "Yuan Shui",
                        "Buyuk",
                        "Goksu",
                        "Waikato",
                        "Parana River",
                        "Eweburn",
                        "Oum Er Rbia",
                        "Hong (Red River)",
                        "Han Jiang",
                        "Mahanadi",
                        "Nakdong",
                        "San Juan",
                        "Panuco",
                        "Mae Nam Ta Pi",
                        "Isser",
                        "Gelevere",
                        "Itajai-Acu",
                        "Senegal",
                        "Ganges - Bramaputra",
                        "Awash",
                        "Madagascar",
                        "Qiantang Jiang",
                        "Yenisei",
                        "Cao'e",
                        "Jacui",
                        "Sefid-Rud",
                        "Colorado",
                        "Yalu Jiang",
                        "Yong",
                        "Mississippi",
                        "Arade",
                        "Orange",
                        "Jialing Jiang",
                        "Lake Chad",
                        "Mouileh",
                        "Tigris",
                        "Rapel River",
                        "Wadi Dayqah",
                        "Jegin",
                        "Rio Tinto",
                        "Pearl",
                        "Cagayan",
                        "Kolyma",
                        "Mahi",
                        "Sassandra",
                        "Iwaki",
                        "Lake Urmia",
                        "Paraguay",
                        "",
                        "Zambezi",
                        "Guyayas",
                        "Lempa",
                        "Fu He",
                        "Cross",
                        "Waipori",
                        "Mediterranean Coast",
                        "Irrawaddy",
                        "Tapi",
                        "Kiso Ibi Nagara",
                        "Yalong Jiang",
                        "East Central Coast",
                        "Shatt al-Arab",
                        "Belize River",
                        "Bhogeshwari",
                        "Magdalena",
                        "South West Coast",
                        "De la Plata",
                        "Jokulsa a Dal",
                        "Lulealven",
                        "Negro",
                        "Kaiwharawhara",
                        "Mearim",
                        "Khyargas Lake",
                        "Dong Nai",
                        "Kamchay",
                        "Uruguay River",
                        "Xiang Jiang",
                        "Indus",
                        "Atchafalaya River",
                        "Xinjiang Uygur",
                        "Ishikari",
                        "Jequitinhonha",
                        "Orinoco",
                        "Patea",
                        "Volta",
                        "Sefad Rad",
                        "Waipapa",
                        "Gan Jiang",
                        "Central West Coast",
                        "Thach Han",
                        "Sanaga",
                        "Rokel",
                        "Bengawan Solo",
                        "Changua",
                        "Juquia-Ribeira",
                        "Bakhtegan Lake",
                        "Shule",
                        "Reventazon-Parismina",
                        "Parana",
                        "Brahmaputra-Meghna",
                        "Zauri",
                        "Saguenay",
                        "Yangtzee",
                        "Rangitaiki",
                        "Rajang",
                        "Omo",
                        "Doce",
                        "Mae Khlong",
                        "Waitaki",
                        "Caspian Sea",
                        "Miomote",
                        "Chao Phraya",
                        "Amu Darya",
                        "Douro River",
                        "Nile",
                        "Song Hong Ha",
                        "Cossey's Creek",
                        "Geum",
                        "Wu Jiang",
                        "Ceyhan",
                        "Narmada",
                        "Shinano",
                        "Dadu He",
                        "Yesil",
                        "Zhu Jiang",
                        "Sharavathi",
                        "Krishna",
                        "Rancherio",
                        "Qiantang",
                        "Agno",
                        "Huang He",
                        "Mahaweli",
                        "Yate",
                        "Ranjang",
                        "Quzil Uzan",
                        "Da Rang",
                        "Mayo",
                        "Harsit",
                        "Balsas",
                        "Amapari",
                        "Pahang",
                        "RÃo Grande de Santiago",
                        "Barak",
                        "La Plata",
                        "Tocantins",
                        "Waiau",
                        "Logan River",
                        "Indian Ocean Coast",
                        "Kocaderre",
                        "Niger",
                        "St Lawrence",
                        "Ougar",
                        "Bhima",
                        "Cobb",
                        "Salween",
                        "Berdan",
                        "Cuanza",
                        "Beijiang",
                        "Wairoa",
                        "Sittang",
                        "Kizilirmak",
                        "Paranaiba",
                        "Prasae",
                        "Liao He",
                        "Ulua",
                        "Ob"),
                List.of("Hydroelectricity",
                        "Pollution control",
                        "Hydroelectricity",
                        "Multipurpose",
                        "Other",
                        "Navigation",
                        "Fisheries",
                        "Flood control",
                        "Water supply",
                        "Irrigation",
                        "Recreation"),
                List.of("2: Good"),
                List.of(1927, 2026),
                List.of("China"),
                new RangeData(-1, -1, -1, 20, -1)

        );
        queryWrapper.in(form.getDamLenM() != null, Barrier::getDamType, form.getDamType())
                .in(form.getMainBasin() != null, Barrier::getMainBasin, form.getMainBasin())
                .in(form.getMainUse() != null, Barrier::getMainUse, form.getMainUse())
                .in(form.getCountry() != null, Barrier::getCountry, form.getCountry())
                .in(form.getQuality() != null, Barrier::getQuality, form.getQuality())
                .between(form.getYear() != null, Barrier::getYearDam, form.getYear().getFirst(), form.getYear().getLast());

        RangeData damLenM = form.getDamLenM();
        queryWrapper.eq(damLenM.getEq() != -1, Barrier::getPowerMw, damLenM.getEq())
                .gt(damLenM.getGt() != -1, Barrier::getPowerMw, damLenM.getGt())
                .ge(damLenM.getGe() != -1, Barrier::getPowerMw, damLenM.getGe())
                .lt(damLenM.getLt() != -1, Barrier::getPowerMw, damLenM.getLt())
                .le(damLenM.getLe() != -1, Barrier::getPowerMw, damLenM.getLe());

        RangeData damHgtM = form.getDamHgtM();
        queryWrapper.eq(damHgtM.getEq() != -1, Barrier::getDamHgtM, damHgtM.getEq())
                .gt(damHgtM.getGt() != -1, Barrier::getDamHgtM, damHgtM.getGt())
                .ge(damHgtM.getGe() != -1, Barrier::getDamHgtM, damHgtM.getGe())
                .lt(damHgtM.getLt() != -1, Barrier::getDamHgtM, damHgtM.getLt())
                .le(damHgtM.getLe() != -1, Barrier::getDamHgtM, damHgtM.getLe());

        List<Barrier> barriers = barrierService.list(queryWrapper);
    }
}
