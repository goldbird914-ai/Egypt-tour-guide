package com.example.data

data class MonumentLocalizedData(
    val name: String,
    val subtitle: String,
    val era: String,
    val city: String,
    val shortDesc: String,
    val detailedHistory: String,
    val audioScript: String,
    val funFact: String
)

data class RouteLocalizedData(
    val title: String,
    val subtitle: String,
    val region: String,
    val description: String
)

data class ArtifactLocalizedData(
    val title: String,
    val originalYear: String,
    val description: String,
    val audioStory: String
)

object MonumentTranslations {

    private val monumentMap: Map<String, Map<AppLanguage, MonumentLocalizedData>> = mapOf(
        "giza_pyramids" to mapOf(
            AppLanguage.ENGLISH to MonumentLocalizedData(
                name = "Giza Pyramids Plateau & Royal Tombs",
                subtitle = "The Sole Surviving Wonder of the Ancient World",
                era = "Old Kingdom (4th Dynasty, ~2560 BC)",
                city = "Giza",
                shortDesc = "The immortal plateau where the Great Pyramids of Khufu, Khafre, and Menkaure rise under the desert sun alongside the guardian Sphinx.",
                detailedHistory = "Built on the Giza plateau around 2560 BC under Pharaoh Khufu, the Great Pyramid remained the tallest human-made structure for more than 3,800 years. Built with 2.3 million megalithic limestone blocks, it demonstrates unmatched astronomical precision and architectural mastery.",
                audioScript = "You are standing before humanity's greatest ancient achievement. Khufu built this colossal pyramid as a stairway to the northern stars, accompanied by Khafre and Menkaure on the golden sands of Giza.",
                funFact = "The Great Pyramid held the record as the world's tallest human-made structure for over 3,800 consecutive years!"
            ),
            AppLanguage.FRENCH to MonumentLocalizedData(
                name = "Plateau des Pyramides de Gizeh",
                subtitle = "L'unique merveille du monde antique encore debout",
                era = "Ancien Empire (IVe dynastie)",
                city = "Gizeh",
                shortDesc = "Le site mythique abritant les grandes pyramides de Khéops, Khéphren et Mykérinos veillées par le Sphinx.",
                detailedHistory = "Érigée vers 2560 av. J.-C. pour le pharaon Khéops, la Grande Pyramide est constituée de 2,3 millions de blocs de calcaire. Elle témoigne du génie astronomique et mathématique de l'Égypte antique.",
                audioScript = "Bienvenue sur le majestueux plateau de Gizeh. Érigées sur les sables d'or, ces pyramides incarnent l'éternité et la grandeur des pharaons.",
                funFact = "La Grande Pyramide est restée le plus haut édifice au monde pendant plus de 3800 ans consécutifs !"
            ),
            AppLanguage.GERMAN to MonumentLocalizedData(
                name = "Pyramiden-Plateau von Gizeh",
                subtitle = "Das einzige erhaltene Weltwunder der Antike",
                era = "Altes Reich (4. Dynastie)",
                city = "Gizeh",
                shortDesc = "Das weltberühmte Felsplateau mit den Pyramiden von Cheops, Chephren und Mykerinos sowie der Großen Sphinx.",
                detailedHistory = "Erbaut um 2560 v. Chr. für Pharao Cheops aus 2,3 Millionen Steinblöcken. Ein Meisterwerk der antiken Astronomie und Baukunst.",
                audioScript = "Sie stehen vor dem größten Monument der antiken Welt. Cheops ließ dieses Bauwerk als Brücke zu den Sternen errichten.",
                funFact = "Die Cheops-Pyramide war über 3.800 Jahre lang das höchste von Menschenhand geschaffene Bauwerk der Erde!"
            ),
            AppLanguage.SPANISH to MonumentLocalizedData(
                name = "Meseta de las Pirámides de Guiza",
                subtitle = "La única maravilla del mundo antiguo superviviente",
                era = "Imperio Antiguo (Dinastía IV)",
                city = "Guiza",
                shortDesc = "La mítica meseta donde se alzan las tres grandes pirámides de Keops, Kefrén y Micerino junto a la enigmática Esfinge.",
                detailedHistory = "Construida hacia el 2560 a.C. bajo el mandato del faraón Keops, con más de 2,3 millones de bloques de piedra caliza cortados con precisión milimétrica.",
                audioScript = "Te encuentras ante el mayor logro de la antigüedad. Keops levantó esta colosal pirámide para alcanzar la eternidad.",
                funFact = "¡La Gran Pirámide fue la estructura más alta construida por el ser humano durante más de 3.800 años seguidos!"
            ),
            AppLanguage.ITALIAN to MonumentLocalizedData(
                name = "Altopiano delle Piramidi di Giza",
                subtitle = "L'unica meraviglia superstite del mondo antico",
                era = "Antico Regno (IV Dinastia)",
                city = "Giza",
                shortDesc = "Il leggendario altopiano dove si ergono le piramidi di Cheope, Chefren e Micerino custodite dalla Grande Sfinge.",
                detailedHistory = "Edificata intorno al 2560 a.C. per il faraone Cheope, la Grande Piramide è composta da 2,3 milioni di blocchi calcarei disposti con incredibile precisione astronomica.",
                audioScript = "Benvenuto davanti al più imponente capolavoro dell'umanità antica, simbolo eterno della civiltà egizia.",
                funFact = "La Grande Piramide è stata la costruzione più alta del mondo per oltre 3.800 anni consecutivi!"
            ),
            AppLanguage.RUSSIAN to MonumentLocalizedData(
                name = "Плато пирамид Гизы и царские гробницы",
                subtitle = "Единственное сохранившееся чудо древнего мира",
                era = "Древнее царство (IV династия)",
                city = "Гиза",
                shortDesc = "Легендарное плато с великими пирамидами Хеопса, Хефрена и Микерина, охраняемое Большим Сфинксом.",
                detailedHistory = "Построена около 2560 г. до н.э. фараоном Хеопсом из 2,3 миллиона известняковых блоков с непревзойденной астрономической точностью.",
                audioScript = "Вы стоите перед величайшим триумфом древнего зодчества. Пирамида Хеопса возведена как лестница к вечным звездам.",
                funFact = "Великая пирамида оставалась высочайшим рукотворным сооружением на Земле более 3800 лет подряд!"
            ),
            AppLanguage.CHINESE to MonumentLocalizedData(
                name = "吉萨金字塔高原与皇家陵寝",
                subtitle = "古代世界七大奇迹中唯一仅存的传世奇迹",
                era = "古王国时期 (第4王朝，约前2560年)",
                city = "吉萨",
                shortDesc = "由胡夫、哈夫拉和门卡乌拉三座大金字塔与守护狮身人面像组成的壮丽古代奇观。",
                detailedHistory = "建于约公元前2560年胡夫法老时期，由230万块巨型石灰石精确砌筑而成。历经3800年一直是人类建造的最高建筑物。",
                audioScript = "您正站在人类古代文明最辉煌的成就面前。胡夫法老在吉萨高原筑起通往永恒星空的石质天梯。",
                funFact = "胡夫大金字塔在长达3800余年的时间里，始终保持着人类建筑史上的最高世界纪录！"
            ),
            AppLanguage.JAPANESE to MonumentLocalizedData(
                name = "ギザの三大ピラミッド高原",
                subtitle = "世界の古代七不思議で唯一現存する奇跡",
                era = "古王国時代（第4王朝、紀元前2560年頃）",
                city = "ギザ",
                shortDesc = "クフ王、カフラー王、メンカウラー王の三大ピラミッドとスフィンクスが佇む悠久の砂漠高原。",
                detailedHistory = "紀元前2560年頃、クフ王によって建造。230万個の巨石を精密に積み上げ、3800年以上にわたり世界一の高さを誇りました。",
                audioScript = "古代世界の頂点に立つギザ高原へようこそ。ピラミッドは永遠の星々へと昇る魂の階段として建てられました。",
                funFact = "クフ王のピラミッドは、3800年以上もの間、人類史上最も高い建造物であり続けました！"
            ),
            AppLanguage.TURKISH to MonumentLocalizedData(
                name = "Gize Piramitleri Platosu ve Kraliyet Mezarları",
                subtitle = "Antik Dünyanın Günümüze Ulaşan Tek Harikası",
                era = "Eski Krallık (4. Hanedan)",
                city = "Gize",
                shortDesc = "Keops, Kefren ve Mikerinos piramitlerinin Sfenks ile yükseldiği ölümsüz antik plato.",
                detailedHistory = "M.Ö. 2560 civarında Firavun Keops için 2,3 milyon kireçtaşı blokla inşa edilmiştir. 3.800 yılı aşkın süre insan yapımı en yüksek yapı olmuştur.",
                audioScript = "İnsanlık tarihinin en büyük antik şaheserinin önündesiniz. Keops piramidi yıldızlara uzanan bir köprü olarak inşa edildi.",
                funFact = "Büyük Piramit, 3.800 yılı aşkın bir süre boyunca dünyanın en yüksek insan yapımı yapısı unvanını korumuştur!"
            ),
            AppLanguage.KOREAN to MonumentLocalizedData(
                name = "기자 피라미드 고원 및 왕실 무덤",
                subtitle = "고대 세계 7대 불가사의 중 유일하게 현존하는 유적",
                era = "고왕국 (제4왕조, 기원전 약 2560년)",
                city = "기자",
                shortDesc = "쿠푸, 카프라, 멘카우라의 3대 피라미드와 스핑크스가 수호하는 이집트의 상징적인 유적지.",
                detailedHistory = "기원전 2560년경 쿠푸 왕을 위해 230만 개의 석회암 블록으로 세워진 거대한 건축물로, 3800년 넘게 세계 최고 높이를 유지했습니다.",
                audioScript = "고대 인류 최고의 건축 걸작 앞에 오신 것을 환영합니다. 쿠푸 왕은 영원한 별을 향한 계단으로 이 피라미드를 세웠습니다.",
                funFact = "쿠푸 대피라미드는 무려 3,800년이 넘는 세월 동안 인류가 건설한 가장 높은 건축물이었습니다!"
            ),
            AppLanguage.PORTUGUESE to MonumentLocalizedData(
                name = "Planalto das Pirâmides de Gizé",
                subtitle = "A única maravilha do mundo antigo ainda de pé",
                era = "Império Antigo (IV Dinastia)",
                city = "Gizé",
                shortDesc = "O lendário platô onde erguem-se as grandes pirâmides de Quéops, Quéfren e Miquerinos ao lado da Esfinge.",
                detailedHistory = "Construída por volta de 2560 a.C. pelo faraó Quéops com 2,3 milhões de blocos calcários, demonstrando notável precisão astronômica.",
                audioScript = "Você está diante da maior conquista arquitetônica da Antiguidade. As pirâmides foram erguidas para tocar a eternidade.",
                funFact = "A Grande Pirâmide foi a construção mais alta do mundo por mais de 3.800 anos consecutivos!"
            ),
            AppLanguage.POLISH to MonumentLocalizedData(
                name = "Płaskowyż Piramid w Gizie",
                subtitle = "Jedyny ocalały cud starożytnego świata",
                era = "Stare Państwo (IV dynastia)",
                city = "Giza",
                shortDesc = "Słynny płaskowyż z piramidami Cheopsa, Chefrena i Mykerinosa strzeżonymi przez Wielkiego Sfinksa.",
                detailedHistory = "Zbudowana około 2560 r. p.n.e. dla faraona Cheopsa z 2,3 miliona bloków kamiennych, stanowi szczyt starożytnej inżynierii.",
                audioScript = "Stoisz przed największym osiągnięciem starożytnej architektury. Piramida powstała jako pomost do wiecznych gwiazd.",
                funFact = "Wielka Piramida była najwyższą budowlą wzniesioną przez człowieka przez ponad 3800 kolejnych lat!"
            ),
            AppLanguage.HINDI to MonumentLocalizedData(
                name = "गीज़ा के पिरामिड पठार और शाही मकबरे",
                subtitle = "प्राचीन विश्व के सात अजूबों में से एकमात्र जीवित आश्चर्य",
                era = "प्राचीन साम्राज्य (चौथा राजवंश, लगभग 2560 ई.पू.)",
                city = "गीज़ा",
                shortDesc = "रेगिस्तान की सुनहरी रेत पर खूफू, खाफ्रे और मेनकॉरे के विशाल पिरामिड और स्फिंक्स।",
                detailedHistory = "लगभग 2560 ईसा पूर्व में राजा खूफू के लिए 23 लाख चूना पत्थर के पत्थरों से निर्मित। 3800 से अधिक वर्षों तक यह विश्व की सबसे ऊंची मानव निर्मित संरचना रही।",
                audioScript = "आप प्राचीन विश्व की सबसे महान उपलब्धि के सामने खड़े हैं। खूफू ने तारों तक पहुंचने की सीढ़ी के रूप में यह पिरामिड बनवाया था।",
                funFact = "गीज़ा का महान पिरामिड 3,800 से अधिक वर्षों तक लगातार दुनिया की सबसे ऊंची मानव निर्मित संरचना बना रहा!"
            )
        ),
        "great_sphinx" to mapOf(
            AppLanguage.ENGLISH to MonumentLocalizedData(
                name = "The Great Sphinx of Giza",
                subtitle = "Oldest and Largest Colossal Mythological Statue",
                era = "Old Kingdom (4th Dynasty, ~2500 BC)",
                city = "Giza",
                shortDesc = "The mythical guardian carved directly from bedrock, merging a lion's raw strength with a pharaoh's supreme wisdom.",
                detailedHistory = "Carved under Pharaoh Khafre around 2500 BC from a single massive limestone outcrop, the Sphinx faces due east towards the rising sun. Between its paws sits the famous Dream Stele of Thutmose IV.",
                audioScript = "Welcome before the eternal guardian 'Hor-em-akhet' (Horus of the Horizon). Listen to the whispers of ancient winds across 4,500 years.",
                funFact = "Traces of original red pigment and royal lapis lazuli prove the Sphinx was once brilliantly painted in vivid pharaonic colors!"
            ),
            AppLanguage.FRENCH to MonumentLocalizedData(
                name = "Le Grand Sphinx de Gizeh",
                subtitle = "Le plus ancien et colossal sphinx du monde antique",
                era = "Ancien Empire (IVe dynastie)",
                city = "Gizeh",
                shortDesc = "Le gardien mythique taillé à même la roche, alliant la force du lion à la sagesse pharaonique.",
                detailedHistory = "Sculpté sous le règne de Khéphren vers 2500 av. J.-C., il mesure 73 mètres de long et fait face au soleil levant. Entre ses pattes repose la célèbre Stèle du Songe.",
                audioScript = "Voici le gardien de l'éternité, le Sphinx royal, symbole de force et de majesté veillant sur les pyramides.",
                funFact = "Des traces de pigments révèlent que le visage du Sphinx était autrefois entièrement peint en rouge royal !"
            ),
            AppLanguage.GERMAN to MonumentLocalizedData(
                name = "Die Große Sphinx von Gizeh",
                subtitle = "Älteste und größte Kolossalstatue der Welt",
                era = "Altes Reich (4. Dynastie)",
                city = "Gizeh",
                shortDesc = "Der mythische Wächter aus gewachsenem Kalkstein mit Löwenkörper und dem Haupt eines weisen Königs.",
                detailedHistory = "Erschaffen unter König Chephren um 2500 v. Chr. Er blickt exakt nach Osten zur aufgehenden Sonne.",
                audioScript = "Willkommen vor dem ewigen Wächter von Gizeh, der seit Jahrtausenden über die Gräber wacht.",
                funFact = "Farbspuren beweisen, dass die Sphinx einst in leuchtendem Königsrot und Lapislazuliblau bemalt war!"
            ),
            AppLanguage.CHINESE to MonumentLocalizedData(
                name = "吉萨狮身人面像 (斯芬克斯)",
                subtitle = "古代世界最古老、最庞大的神话巨像",
                era = "古王国时期 (第4王朝，约前2500年)",
                city = "吉萨",
                shortDesc = "由一整块天然石灰岩山体雕琢而成的永恒守护者，象征力量与智慧的结合。",
                detailedHistory = "约建于公元前2500年哈夫拉法老统治时期，长73米，高20米，正对日出的东方。双爪之间立有著名的图特摩斯四世记梦碑。",
                audioScript = "欢迎来到永恒守护者‘地平线上的荷鲁斯’面前。狮身人面像在此静立4500年，见证世事变迁。",
                funFact = "科学检测表明，狮身人面像在古王国时期曾被涂上鲜艳的面部红颜料和蓝金色头巾！"
            ),
            AppLanguage.JAPANESE to MonumentLocalizedData(
                name = "ギザの大スフィンクス",
                subtitle = "古代世界最古・最大の守護神像",
                era = "古王国時代（第4王朝、紀元前2500年頃）",
                city = "ギザ",
                shortDesc = "ライオンの力強い肉体とファラオの英知が融合した、石灰岩の一枚岩から彫り出された守護獣。",
                detailedHistory = "カフラー王の時代に彫られ、東の朝日を真っ直ぐ見つめています。前足の間にはツトメス4世の「夢の碑」が鎮座しています。",
                audioScript = "古代の守護神スフィンクスの前にようこそ。4500年の時を超えて、ファラオの威厳を今に伝えています。",
                funFact = "スフィンクスの顔は、古代には鮮やかな赤色で彩色されていたことが判明しています！"
            ),
            AppLanguage.RUSSIAN to MonumentLocalizedData(
                name = "Большой Сфинкс в Гизе",
                subtitle = "Древнейшая монументальная статуя на Земле",
                era = "Древнее царство (IV династия)",
                city = "Гиза",
                shortDesc = "Величественный страж с телом льва и лицом царя, высеченный из цельной известняковой скалы.",
                detailedHistory = "Создан при фараоне Хефрене около 2500 г. до н.э. Длина 73 метра, обращен лицом строго на восток к восходу солнца.",
                audioScript = "Перед вами вечный страж пирамид, воплощение львиной мощи и мудрости фараона.",
                funFact = "На лице Сфинкса ученые обнаружили следы оригинальной царской красной краски!"
            )
        ),
        "karnak_temple" to mapOf(
            AppLanguage.ENGLISH to MonumentLocalizedData(
                name = "Karnak Temple Complex & Great Hypostyle",
                subtitle = "Largest Religious Sanctuary in the Ancient World",
                era = "Middle & New Kingdom (Over 2,000 Years of Construction)",
                city = "Luxor",
                shortDesc = "The divine city of temples dedicated to Amun-Ra, housing the towering 134-column Great Hypostyle Hall and sacred lake.",
                detailedHistory = "Spanning 2,000 years of construction by over 30 pharaohs, Karnak represents the religious epicenter of ancient Egypt. The 5,000 sq meter Hypostyle Hall features 134 colossal columns rising up to 21 meters.",
                audioScript = "Enter Ipet-Sut, the 'Most Select of Places'. Feel the immense power of Amun-Ra as towering columns rise like a petrified forest of stone papyrus.",
                funFact = "Karnak is so massive that the Cathedral of Notre Dame in Paris could fit inside its main hypostyle hall ten times over!"
            ),
            AppLanguage.FRENCH to MonumentLocalizedData(
                name = "Complexe des Temples de Karnak",
                subtitle = "Le plus grand sanctuaire religieux de l'Antiquité",
                era = "Moyen et Nouvel Empire",
                city = "Louxor",
                shortDesc = "La cité des dieux consacrée à Amon-Rê, avec sa prodigieuse salle hypostyle de 134 colonnes géantes.",
                detailedHistory = "Construit sur deux millénaires par plus de 30 pharaons, Karnak est le plus vaste complexe religieux jamais bâti par l'homme.",
                audioScript = "Pénétrez dans le domaine sacré d'Amon-Rê. Ses colonnes s'élèvent vers le ciel comme une forêt de papyrus de pierre.",
                funFact = "La grande salle hypostyle de Karnak pourrait contenir dix fois la cathédrale Notre-Dame de Paris !"
            ),
            AppLanguage.GERMAN to MonumentLocalizedData(
                name = "Karnak-Tempelkomplex & Säulenhalle",
                subtitle = "Das größte religiöse Bauwerk der Antike",
                era = "Mittleres und Neues Reich",
                city = "Luxor",
                shortDesc = "Das Heiligtum des Amun-Re mit der weltberühmten Großen Hypostylhalle mit 134 monumentalen Säulen.",
                detailedHistory = "Über 2.000 Jahre hinweg von mehr als 30 Pharaonen erweitert. Ein unvergleichliches Zeugnis altägyptischer Frömmigkeit.",
                audioScript = "Treten Sie ein in den heiligsten aller Orte. Die Säulen ragen wie ein versteinerter Urwald empor.",
                funFact = "Die Große Säulenhalle von Karnak ist groß genug, um die Kathedrale Notre-Dame von Paris zehnmal aufzunehmen!"
            ),
            AppLanguage.CHINESE to MonumentLocalizedData(
                name = "卡纳克神庙建筑群与多柱大厅",
                subtitle = "古代世界规模最为宏伟的宗教圣殿",
                era = "中王国与新王国时期 (逾2000年营建史)",
                city = "卢克索",
                shortDesc = "奉献给阿蒙·拉大神的宏伟神城，拥有包含134根擎天巨柱的柱厅、哈特谢普苏特方尖碑与圣湖。",
                detailedHistory = "前后由30余位法老历经2000年持续扩建，是古埃及最重要的宗教中枢。巨柱大厅占地5000平方米，柱高可达21米。",
                audioScript = "您已步入古埃及人称为‘至选之所’的卡纳克。134根巍峨石柱犹如石化的纸莎草森林般耸立于天地之间。",
                funFact = "卡纳克神庙的多柱大厅面积之大，足以容纳十座巴黎圣母院大教堂！"
            ),
            AppLanguage.JAPANESE to MonumentLocalizedData(
                name = "カルナック神殿複合体・大列柱室",
                subtitle = "古代世界最大の宗教的聖域",
                era = "中王国・新王国時代（2000年以上にわたる建設）",
                city = "ルクソール",
                shortDesc = "アメン・ラー神に捧げられた壮大な神殿都市。134本の大列柱が立ち並ぶ大列柱室は圧巻。",
                detailedHistory = "30人以上のファラオによって2000年以上にわたり増改築が重ねられました。最大の見どころは大列柱室です。",
                audioScript = "アメン神の神聖なる領域へ。まるで石のパピルスの森に迷い込んだかのような圧倒的な空間が広がります。",
                funFact = "カルナックの大列柱室の広さは、パリのノートルダム大聖堂が10棟すっぽり入るほどの巨大さです！"
            ),
            AppLanguage.RUSSIAN to MonumentLocalizedData(
                name = "Храмовый комплекс Карнак и колонный зал",
                subtitle = "Крупнейшее религиозное святилище древнего мира",
                era = "Среднее и Новое царство",
                city = "Луксор",
                shortDesc = "Священный город Амона-Ра с грандиозным гипостильным залом из 134 исполинских колонн.",
                detailedHistory = "Строился более двух тысяч лет с участием более 30 фараонов. Площадь гипостильного зала — 5000 кв. метров.",
                audioScript = "Добро пожаловать в святилище Амона-Ра. Колонны взмывают в небо словно окаменевший папирусный лес.",
                funFact = "В великом гипостильном зале Карнака мог бы свободно поместиться собор Парижской Богоматери десять раз!"
            )
        )
    )

    fun getMonumentTranslation(id: String, lang: AppLanguage): MonumentLocalizedData? {
        return monumentMap[id]?.get(lang)
    }

    private val routeMap: Map<String, Map<AppLanguage, RouteLocalizedData>> = mapOf(
        "route_giza_saqqara" to mapOf(
            AppLanguage.ENGLISH to RouteLocalizedData(
                title = "Giza & Saqqara Pyramids Trail",
                subtitle = "From the Step Pyramid to the Giza Plateau",
                region = "Giza & Greater Cairo",
                description = "Trace the complete evolution of royal pyramid architecture across dynasties."
            ),
            AppLanguage.FRENCH to RouteLocalizedData(
                title = "Circuit des Pyramides de Gizeh et Saqqarah",
                subtitle = "De la pyramide à degrés au plateau de Gizeh",
                region = "Gizeh et Grand Caire",
                description = "Explorez l'évolution complète de l'architecture des pyramides à travers les âges."
            ),
            AppLanguage.GERMAN to RouteLocalizedData(
                title = "Pyramiden-Route: Gizeh & Sakkara",
                subtitle = "Von der Stufenpyramide bis nach Gizeh",
                region = "Gizeh & Kairo",
                description = "Erleben Sie die architektonische Entwicklung der Pharaonengräber hautnah."
            ),
            AppLanguage.SPANISH to RouteLocalizedData(
                title = "Ruta de las Pirámides de Guiza y Saqqara",
                subtitle = "De la pirámide escalonada a las grandes pirámides",
                region = "Guiza y El Gran Cairo",
                description = "Descubre la evolución completa de la arquitectura funeraria de los faraones."
            ),
            AppLanguage.CHINESE to RouteLocalizedData(
                title = "吉萨与萨卡拉金字塔探秘路线",
                subtitle = "从阶梯金字塔到三大巨型金字塔的演进历程",
                region = "吉萨与大开罗地区",
                description = "带您系统追寻古埃及法老金字塔建筑从初创到顶峰的辉煌历程。"
            ),
            AppLanguage.JAPANESE to RouteLocalizedData(
                title = "ギザ＆サッカラ ピラミッド完全巡礼ルート",
                subtitle = "階段ピラミッドからギザの三大巨塔へ",
                region = "ギザ＆カイロ近郊",
                description = "古代エジプトのピラミッド建築の進化の歴史を辿る王道コース。"
            ),
            AppLanguage.RUSSIAN to RouteLocalizedData(
                title = "Маршрут: Пирамиды Гизы и Саккара",
                subtitle = "От ступенчатой пирамиды Джосера до плато Гиза",
                region = "Гиза и Каир",
                description = "Полная эволюция древнеегипетского пирамидального зодчества за один день."
            )
        ),
        "route_luxor_thebes" to mapOf(
            AppLanguage.ENGLISH to RouteLocalizedData(
                title = "Ancient Thebes & Luxor Temples Trail",
                subtitle = "East & West Banks of Thebes in Luxor",
                region = "Luxor & Upper Egypt",
                description = "Walk among gods and pharaohs through Karnak, Luxor Temple, and the Valley of the Kings."
            ),
            AppLanguage.FRENCH to RouteLocalizedData(
                title = "Circuit de Thèbes Antique et Louxor",
                subtitle = "Rives Est et Ouest de Louxor",
                region = "Louxor et Haute-Égypte",
                description = "Entre les dieux et les pharaons à travers Karnak et la Vallée des Rois."
            ),
            AppLanguage.CHINESE to RouteLocalizedData(
                title = "古底比斯与卢克索东西岸神庙路线",
                subtitle = "探寻卡纳克、卢克索神庙与帝王谷",
                region = "卢克索与上埃及",
                description = "穿梭于日出东岸的宏伟神庙与日落西岸的永恒法老墓室之间。"
            ),
            AppLanguage.JAPANESE to RouteLocalizedData(
                title = "古代テーベ＆ルクソール東西岸紀行",
                subtitle = "カルナック神殿と王家の谷を巡る",
                region = "ルクソール＆上エジプト",
                description = "東岸の巨大神殿群と西岸のファラオの眠る谷を巡る贅沢なルート。"
            )
        )
    )

    fun getRouteTranslation(id: String, lang: AppLanguage): RouteLocalizedData? {
        return routeMap[id]?.get(lang)
    }

    private val artifactMap: Map<String, Map<AppLanguage, ArtifactLocalizedData>> = mapOf(
        "ar_sphinx" to mapOf(
            AppLanguage.ENGLISH to ArtifactLocalizedData(
                title = "The Great Sphinx in Royal Pharaonic Colors",
                originalYear = "~2500 BC - Reign of King Khafre",
                description = "3D hologram reconstruction of the Sphinx with its braided divine beard and ceremonial Nemes headdress.",
                audioStory = "Stand before the 73-meter monumental limestone guardian restored to its 4,500-year-old glory."
            ),
            AppLanguage.FRENCH to ArtifactLocalizedData(
                title = "Le Grand Sphinx aux Couleurs Royales",
                originalYear = "Vers 2500 av. J.-C. - Règne de Khéphren",
                description = "Reconstitution holographique en 3D avec sa barbe tressée et son némès royal.",
                audioStory = "Découvrez le Sphinx tel qu'il resplendissait il y a 4500 ans dans ses couleurs antiques."
            ),
            AppLanguage.CHINESE to ArtifactLocalizedData(
                title = "狮身人面像：法老皇家彩绘全貌",
                originalYear = "约公元前2500年 - 哈夫拉法老时期",
                description = "3D全息还原斯芬克斯雕像最初的编织神圣胡须与红金彩绘王者头巾。",
                audioStory = "重温这座长73米、高20米的巨型石雕重现4500年前昔日辉煌的时刻。"
            ),
            AppLanguage.JAPANESE to ArtifactLocalizedData(
                title = "大スフィンクス：古代ファラオの極彩色復元",
                originalYear = "紀元前2500年頃 - カフラー王治世",
                description = "神聖なあごひげと色鮮やかなネメス頭巾をまとったスフィンクスの3Dホログラム復元。",
                audioStory = "4500年前の本来の彩色を再現した壮大なスフィンクスの姿をご体験ください。"
            ),
            AppLanguage.RUSSIAN to ArtifactLocalizedData(
                title = "Большой Сфинкс в царских красках",
                originalYear = "~2500 г. до н.э. - эпоха Хефрена",
                description = "3D-голографическая реконструкция Сфинкса с бородой богов и царским платком немес.",
                audioStory = "Взгляните на Сфинкса таким, каким его видели древние египтяне 4500 лет назад."
            )
        ),
        "ar_tut_mask" to mapOf(
            AppLanguage.ENGLISH to ArtifactLocalizedData(
                title = "Golden Mask of Tutankhamun",
                originalYear = "1323 BC - 18th Dynasty",
                description = "11 kilograms of solid 22-carat gold inlaid with lapis lazuli, carnelian, and sacred protective uraeus.",
                audioStory = "Witness the eternal golden face of the boy king, crafted to protect him through the afterlife."
            ),
            AppLanguage.FRENCH to ArtifactLocalizedData(
                title = "Masque d'Or de Toutânkhamon",
                originalYear = "1323 av. J.-C. - XVIIIe dynastie",
                description = "11 kilos d'or pur 22 carats incrusté de lapis-lazuli et de pierres précieuses.",
                audioStory = "Admirez le chef-d'œuvre absolu de l'orfèvrerie égyptienne protégeant le jeune pharaon."
            ),
            AppLanguage.CHINESE to ArtifactLocalizedData(
                title = "图坦卡蒙黄金面具",
                originalYear = "公元前1323年 - 第18王朝",
                description = "重达11公斤的22K纯金打造，镶嵌阿富汗青金石、红玉髓与代表上下埃及统一的神圣徽记。",
                audioStory = "聆听少年法老黄金面具背后的不朽传奇，感受古埃及金银器工艺的巅峰之作。"
            ),
            AppLanguage.JAPANESE to ArtifactLocalizedData(
                title = "ツタンカーメンの黄金のマスク",
                originalYear = "紀元前1323年 - 第18王朝",
                description = "重さ11kgの22金無垢製。ラピスラズリとカーネリアンが象嵌された古代エジプト美術の最高峰。",
                audioStory = "永遠の命を願って作られた少年王ツタンカーメンの黄金の輝きを体感してください。"
            )
        )
    )

    fun getArtifactTranslation(id: String, lang: AppLanguage): ArtifactLocalizedData? {
        return artifactMap[id]?.get(lang)
    }
}
