package cc.ixcc.novelthree.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import cc.ixcc.novelthree.treader.AppContext;

/**
 * Created by cxf on 2018/9/17.
 * SharedPreferences 封装
 */

public class SpUtil {
    public static final String CHANNEL = "channel";
    public static final String RECOMMEND = "recommend";
    public static final String UID = "uid";
    public static final String MEID = "MEID";
    public static final String DBUID = "dbuid";
    public static final String TOKEN = "token";
    public static final String APPSYSTEM = "appsystem";
    public static final String USER_INFO = "userInfo";
    public static final String CONFIG = "config";
    public static final String Auto_Login = "autologin";
    public static final PayType CUR_PAYTYPE = PayType.GOOGLE;

    public enum PayType {
        GOOGLE,
        CHUANYIN
    }

    public static String GetCountry() {
        String Country = "CN";

        TelephonyManager tel = (TelephonyManager) AppContext.sInstance.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tel.getNetworkOperator();

        if (!TextUtils.isEmpty(networkOperator)) {
            int mcc = Integer.parseInt(networkOperator.substring(0, 3));

            switch (mcc) {
                case 202:
                    Country = "GR";
                    break;
                case 204:
                    Country = "NL";
                    break;
                case 206:
                    Country = "BE";
                    break;
                case 208:
                    Country = "FR";
                    break;
                case 213:
                    Country = "AD";
                    break;
                case 214:
                    Country = "ES";
                    break;
                case 216:
                    Country = "HU";
                    break;
                case 218:
                    Country = "BA";
                    break;
                case 219:
                    Country = "HR";
                    break;
                case 220:
                    Country = "CS";
                    break;
                case 222:
                    Country = "IT";
                    break;
                case 226:
                    Country = "RO";
                    break;
                case 228:
                    Country = "CH";
                    break;
                case 230:
                    Country = "CZ";
                    break;
                case 231:
                    Country = "SK";
                    break;
                case 232:
                    Country = "AT";
                    break;
                case 234:
                    Country = "GB";
                    break;
                case 238:
                    Country = "DK";
                    break;
                case 240:
                    Country = "SE";
                    break;
                case 242:
                    Country = "NO";
                    break;
                case 244:
                    Country = "FI";
                    break;
                case 246:
                    Country = "LT";
                    break;
                case 247:
                    Country = "LV";
                    break;
                case 248:
                    Country = "EE";
                    break;
                case 250:
                    Country = "RU";
                    break;
                case 255:
                    Country = "UA";
                    break;
                case 257:
                    Country = "BY";
                    break;
                case 259:
                    Country = "MD";
                    break;
                case 260:
                    Country = "PL";
                    break;
                case 262:
                    Country = "DE";
                    break;
                case 266:
                    Country = "GI";
                    break;
                case 268:
                    Country = "PT";
                    break;
                case 270:
                    Country = "LU";
                    break;
                case 272:
                    Country = "IE";
                    break;
                case 274:
                    Country = "IS";
                    break;
                case 276:
                    Country = "AL";
                    break;
                case 278:
                    Country = "MT";
                    break;
                case 280:
                    Country = "CY";
                    break;
                case 282:
                    Country = "GE";
                    break;
                case 283:
                    Country = "AM";
                    break;
                case 284:
                    Country = "BG";
                    break;
                case 286:
                    Country = "TR";
                    break;
                case 288:
                    Country = "FO";
                    break;
                case 290:
                    Country = "GL";
                    break;
                case 293:
                    Country = "SI";
                    break;
                case 294:
                    Country = "MK";
                    break;
                case 295:
                    Country = "LI";
                    break;
                case 302:
                    Country = "CA";
                    break;
                case 310:
                    Country = "US";
                    break;
                case 334:
                    Country = "MX";
                    break;
                case 338:
                    Country = "JM";
                    break;
                case 340:
                    Country = "FW";
                    break;
                case 342:
                    Country = "BB";
                    break;
                case 344:
                    Country = "AG";
                    break;
                case 346:
                    Country = "KY";
                    break;
                case 352:
                    Country = "GD";
                    break;
                case 362:
                    Country = "AN";
                    break;
                case 363:
                    Country = "AW";
                    break;
                case 368:
                    Country = "CU";
                    break;
                case 370:
                    Country = "DO";
                    break;
                case 374:
                    Country = "TT";
                    break;
                case 400:
                    Country = "AZ";
                    break;
                case 401:
                    Country = "KZ";
                    break;
                case 402:
                    Country = "BT";
                    break;
                case 404:
                    Country = "IN";
                    break;
                case 410:
                    Country = "PK";
                    break;
                case 412:
                    Country = "AF";
                    break;
                case 413:
                    Country = "LK";
                    break;
                case 414:
                    Country = "MM";
                    break;
                case 415:
                    Country = "LB";
                    break;
                case 416:
                    Country = "JO";
                    break;
                case 417:
                    Country = "SY";
                    break;
                case 418:
                    Country = "IQ";
                    break;
                case 419:
                    Country = "KW";
                    break;
                case 420:
                    Country = "SA";
                    break;
                case 421:
                    Country = "YE";
                    break;
                case 422:
                    Country = "OM";
                    break;
                case 424:
                    Country = "AE";
                    break;
                case 425:
                    Country = "IL";
                    break;
                case 426:
                    Country = "BH";
                    break;
                case 427:
                    Country = "QA";
                    break;
                case 428:
                    Country = "MN";
                    break;
                case 429:
                    Country = "NP";
                    break;
                case 432:
                    Country = "IR";
                    break;
                case 434:
                    Country = "UZ";
                    break;
                case 437:
                    Country = "KG";
                    break;
                case 438:
                    Country = "TM";
                    break;
                case 452:
                    Country = "VN";
                    break;
                case 454:
                    Country = "HK";
                    break;
                case 456:
                    Country = "KH";
                    break;
                case 457:
                    Country = "LA";
                    break;
                case 460:
                    Country = "CN";
                    break;
                case 466:
                    Country = "TW";
                    break;
                case 467:
                    Country = "KP";
                    break;
                case 470:
                    Country = "BD";
                    break;
                case 472:
                    Country = "MV";
                    break;
                case 502:
                    Country = "MY";
                    break;
                case 505:
                    Country = "AU";
                    break;
                case 510:
                    Country = "ID";
                    break;
                case 515:
                    Country = "PH";
                    break;
                case 520:
                    Country = "TH";
                    break;
                case 525:
                    Country = "SG";
                    break;
                case 528:
                    Country = "BN";
                    break;
                case 530:
                    Country = "NZ";
                    break;
                case 539:
                    Country = "TO";
                    break;
                case 541:
                    Country = "VU";
                    break;
                case 542:
                    Country = "FJ";
                    break;
                case 544:
                    Country = "AS";
                    break;
                case 546:
                    Country = "NC";
                    break;
                case 547:
                    Country = "PF";
                    break;
                case 550:
                    Country = "FM";
                    break;
                case 602:
                    Country = "EG";
                    break;
                case 603:
                    Country = "DZ";
                    break;
                case 604:
                    Country = "MA";
                    break;
                case 605:
                    Country = "TN";
                    break;
                case 607:
                    Country = "GM";
                    break;
                case 608:
                    Country = "SN";
                    break;
                case 609:
                    Country = "MR";
                    break;
                case 610:
                    Country = "ML";
                    break;
                case 611:
                    Country = "GN";
                    break;
                case 612:
                    Country = "CI";
                    break;
                case 613:
                    Country = "BF";
                    break;
                case 614:
                    Country = "NE";
                    break;
                case 615:
                    Country = "TG";
                    break;
                case 616:
                    Country = "BJ";
                    break;
                case 617:
                    Country = "MU";
                    break;
                case 618:
                    Country = "LR";
                    break;
                case 620:
                    Country = "GH";
                    break;
                case 621:
                    Country = "NG";
                    break;
                case 622:
                    Country = "TD";
                    break;
                case 624:
                    Country = "CM";
                    break;
                case 625:
                    Country = "CV";
                    break;
                case 626:
                    Country = "ST";
                    break;
                case 627:
                    Country = "GQ";
                    break;
                case 628:
                    Country = "GA";
                    break;
                case 629:
                    Country = "CG";
                    break;
                case 630:
                    Country = "CD";
                    break;
                case 631:
                    Country = "AO";
                    break;
                case 633:
                    Country = "SC";
                    break;
                case 634:
                    Country = "SD";
                    break;
                case 635:
                    Country = "RW";
                    break;
                case 636:
                    Country = "ET";
                    break;
                case 637:
                    Country = "SO";
                    break;
                case 639:
                    Country = "KE";
                    break;
                case 640:
                    Country = "TZ";
                    break;
                case 641:
                    Country = "UG";
                    break;
                case 642:
                    Country = "BI";
                    break;
                case 646:
                    Country = "MG";
                    break;
                case 647:
                    Country = "RE";
                    break;
                case 648:
                    Country = "ZW";
                    break;
                case 649:
                    Country = "NA";
                    break;
                case 650:
                    Country = "MW";
                    break;
                case 651:
                    Country = "LS";
                    break;
                case 652:
                    Country = "BW";
                    break;
                case 653:
                    Country = "SZ";
                    break;
                case 654:
                    Country = "ZM";
                    break;
                case 655:
                    Country = "ZA";
                    break;
                case 702:
                    Country = "BZ";
                    break;
                case 706:
                    Country = "SV";
                    break;
                case 710:
                    Country = "NI";
                    break;
                case 712:
                    Country = "CR";
                    break;
                case 716:
                    Country = "PE";
                    break;
                case 722:
                    Country = "AR";
                    break;
                case 724:
                    Country = "BR";
                    break;
                case 730:
                    Country = "CL";
                    break;
                case 734:
                    Country = "VE";
                    break;
                case 736:
                    Country = "BO";
                    break;
                case 744:
                    Country = "PY";
                    break;
                case 746:
                    Country = "SR";
                    break;
                default:
                    Country = "CN";
                    break;
            }
        }

        return Country;
    }
}
