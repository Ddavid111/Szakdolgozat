package com.example.proba.service;

import com.example.proba.dao.ThesisDao;
import com.example.proba.dao.TopicDao;
import com.example.proba.entity.Thesis;
import com.example.proba.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private ThesisDao thesisDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Topic> getTopics() {
        return (List<Topic>) topicDao.findAll();
    }

    public Topic addTopics(Topic topics)
    {
        Topic topic = topicDao.save(topics);
        Thesis actualThesis = new Thesis();
        actualThesis.setTopics(topics);
        actualThesis.setId(topic.getId());
        thesisDao.save(actualThesis);

        return topic;
    }

    public void chooseTopic(Object topicId, Object thesisId, Object topic_score) {
        String query = "UPDATE THESIS SET topic_id=" + topicId + ", topic_score=" + topic_score + " WHERE id=" + thesisId;
        jdbcTemplate.update(query);
    }

    public void initTopics() {
        if(thesisDao.getCountOfRecords() <= 0)
        {
            topicDao.deleteAll();
            Topic[] topics = new Topic[27];
            String[] topicNames = {"Adatok, adattípusok, adatműveletek és adatstruktúrák. Szám adattípus.\n" +
                    "Számrendszerek, konverziók. A logikai, halmaz, karakter, sztring absztrakt adattípusok és\n" +
                    "realizációjuk. A tömb (vektor, mátrix), rekord, egyéb absztrakt adattípusok.",
                    "Az algoritmus. Iteratív és rekurzív algoritmus. A számítógépes memória. Adat és program.\n" +
                            "Verem és procedúra. Az algoritmus lejegyzése. A folyamatábra és a pszeudokód. Elemi\n" +
                            "algoritmusok.",
                    "Strukturált programozás. Programgráf, valódi program, vezérlőgráf lebontása, strukturált\n" +
                            "program és annak formulája. Strukturált programgráf kialakítása, struktogram. Ciklikus\n" +
                            "bonyolultság és egyéb bonyolultsági tételek.",
                    "Számelméleti algoritmusok. Legnagyobb közös osztó, euklideszi és kibővített euklideszi\n" +
                            "algoritmus, lineáris kongruencia egyenletek. Multiplikatív inverz, moduláris hatványozás,\n" +
                            "Fermat prímteszt. RSA.\n",
                    "Rendezések: Beszúró rendezés. Az oszd meg és uralkodj elv. Összefésülő rendezés. Gyors\n" +
                            "rendezés. Buborék rendezés. Shell rendezés. Minimum kiválasztásos rendezés. Négyzetes\n" +
                            "rendezés. Lineáris idejű rendezések: leszámláló rendezés, számjegyes rendezés. Időelemzéseik.\n" +
                            "Az összehasonlító rendezések időtétele.",
                    "Gráf algoritmusok. Szélességi keresés. Mélységi keresés. Topológikus rendezés. Optimum\n" +
                            "feladatok fákon (Minimális feszítőfák, a Kruskal és Prim algoritmus). Legrövidebb utak\n" +
                            "meghatározása (Dijkstra algoritmus, Bellman-Ford algoritmus. Floyd-Warshall algoritmus)",
                    "Párhuzamos algoritmusok alapfogalmai, hatékonysági mértékek. Párhuzamos gépek:\n" +
                            "prefixszámítás, determinisztikus tömbrangsorolás, összefésülés, kiválasztás, rendezés.",
                    "Rácsok: csomagirányítás, üzenetszórás, prefixszámítás. Hiperkocka, pillangó hálózat, hálózatok\n" +
                            "beágyazása, csomagirányítás. Szinkronizált hálózat: vezetőválasztási algoritmusok.",
                    "A párhuzamos algoritmusok elkészítésének és implementálásának technikái és problémái. A\n" +
                            "Multi-Pascal nyelv lehetőségeinek bemutatása néhány példán keresztül. A PVM bemutatása a\n" +
                            "\"hello\" és a \"forkjoin\" mintaprogramok segítségével.",
                    "A Turing gép fogalma, működése. A RAM-gép. Boole-függvények és logikai hálózatok",
                    "Algoritmikus eldönthetőség. Church-tézis. Rekurzív és rekurzívan felsorolható nyelvek, rekurzív\n" +
                            "illetve parciálisan rekurzív függvények. Nevezetes nyelvek (Az R, Re, coR, coRE\n" +
                            "nyelvosztályok és ezek kapcsolata) és bonyolultságuk. Algoritmikusan eldönthetetlen\n" +
                            "problémák. Polinomiális idejű algoritmusok.",
                    "Nemdeterminisztikus Turing gépek, Az NP és a coNP nyelvosztály. Példák NP-beli nyelvekre.\n" +
                            "A tanú-tétel. Nemdeterminisztikus algoritmusok bonyolultsága. NP-teljesség, Cook-tétel.\n" +
                            "Néhány NP-teljes probléma, Cook-Levin tétel\n",
                    "A Java programozási nyelv története, alapvető jellemzői. Java platformok, a JDK.\n" +
                            "Osztálydefiníció. Hivatkozás az osztály elemeire. Példányosítás. Hozzáférési kategóriák és\n" +
                            "használatuk. A this pszeudó változó. Metódusnév túlterhelés. Konstruktor.",
                    "Szemétgyűjtő mechanizmus. A finalize metódus. Csomagok és fordítási egységek.\n" +
                            "Osztályváltozó, osztály metódus. final minősítésű adattag. Öröklődés. Statikus és dinamikus\n" +
                            "típus. A protected hozzáférési kategória. Konstruktorok és az öröklődés. final minősítésű\n" +
                            "osztály.",
                    "A metódus felüldefinálás - alapszabályok. A metódus felüldefinálás, mint a polimorfizmus\n" +
                            "implementációja. A final minősítésű metódus. Absztrakt osztályok és metódusok. Az interface és\n" +
                            "az instanceof operátor. Kivételkezelés.",
                    "Az operációs rendszerbeli folyamat (processz) fogalom. Folyamat kontextus. A fonál fogalom. A\n" +
                            "processz állapotok, állapotváltások, processz futási módok. Taszk és fonál állapotok,\n" +
                            "állapotátmenetek.",
                    "A memória menedzselés feladatai. Memória, mint erőforrás. Címleképzés fajták. Lapozós\n" +
                            "virtuális memória menedzselés működése. Allokálás, nyilvántartás, címleképzés. Laphiba\n" +
                            "fogalma, kezelése, kilapozási stratégiák. Előnyök, hátrányok.\n",
                    "Fájlrendszer megvalósítási feladatok. Jegyzékszerkezetek. Szabad blokk menedzselési\n" +
                            "lehetőségek. Fájl attribútum rögzítési lehetőségek (ezen belül fájl testet képező blokkok rögzítési\n" +
                            "lehetőségei).",
                    "Relációs adatmodell, relációs struktúra és integritási feltételek. Relációs adatmodell műveleti\n" +
                            "része, relációs algebra",
                    "Az SQL szabvány relációs kezelő nyelv bemutatása, a DDL, DML és a SELECT utasítások\n" +
                            "használata. Az SQL92 szabvány további elemei.",
                    "Adatkezelés és adatbáziskezelés alapfogalmai, fileszervezési módszerek, B-fa index; adatbázis\n" +
                            "architektúra; Adatmodellek, SDM modellek áttekintése, ER modell, konverzió és normalizálás",
                    "Számítógép-hálózatokhoz kötődő alapfogalmak és az ISO-OSI hivatkozási modell: Topológia és\n" +
                            "méret szerinti osztályozásuk. Kapcsolástechnika szerinti osztályozásuk (vonalkapcsolás,\n" +
                            "üzenetkapcsolás, csomagkapcsolás és virtuális vonalkapcsolás). Az ISO-OSI hivatkozási modell\n" +
                            "szerkezete, rétegei és azok főbb funkciói",
                    "A számítógép-hálózatok ISO-OSI hivatkozási modell adatkapcsolati rétegének közeghozzáférési\n" +
                            "alrétege: A főbb csatorna-megosztási osztályok (statikus - dinamikus, dinamikus versengő -\n" +
                            "determinisztikus) bemutatása és azok összevetése. Az IEEE 802.3 és az Ethernet (CSMA/CD)\n" +
                            "keretformátum, MAC címek, támogatott közegek és sebességek bemutatása, működés duplex\n" +
                            "csatorna esetén. Az IEEE 802.11 WLAN általános felépítése, az Access Point (AP) főbb\n" +
                            "funkciói. Az alkalmazott közeghozzáférési módszer (CSMA/CA), Distributed Coordination\n" +
                            "Function (DCF) és Point Coordination Function (PCF) üzemmódok.",
                    "A TCP/IP protokoll szövet és az Internet: Az Internet hivatkozási modell (DoD) és az ISO-OSI\n" +
                            "hivatkozási modell összevetése. A TCP/IP protokoll szövet főbb részei (ARP, RARP, IP, ICMP,\n" +
                            "TCP, UDP) és azok funkcióik. Az Internet címzés és címosztályok: IPv4 címosztályok, maszk,\n" +
                            "subnet, supernet, osztály nélküli címzés (CIDR Classless Inter-Domain Routing), és a változó\n" +
                            "alhálózat méretek (VLSM Variable Length Subnet Mask); címek kiosztása, lokális címek és a\n" +
                            "címfordítás (NAT). Az IPv6 címek, IPv6 cím típusok (unicast, multicast, anycast; link local,\n" +
                            "unique local, aggregatable global unicast address).",
                    "Szoftvertechnológia és a szoftverfolyamat fogalma. A szoftverfolyamat fázisok részletes\n" +
                            "bemutatása: Szoftverspecifikáció, tervezés, implementáció, validáció és evolúció. Az evolúciós\n" +
                            "modell ismertetése.\n",
                    "Szoftverkövetelmény fogalma és osztályozásai. Követelmények általános problémái. Strukturált\n" +
                            "természetes nyelv. OO felbontás jellemzői. Vezérlési stílusok.",
                    "UML diagramok bemutatása: Use case és osztálydiagram. A szekvencia diagram.\n" +
                            "Állapotdiagram és aktivitás diagram. Komponens diagram."

            };
            for (int i = 0; i < topics.length; i++) {
                topics[i] = new Topic();
                topics[i].setTopic((i + 1) + ".  " + topicNames[i]);
                topicDao.save(topics[i]);
            }
        }
    }
}
