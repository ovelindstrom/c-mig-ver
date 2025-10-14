///**
// * Skapad 2007-feb-02
// * @author Jonas Öhrnell (csn7821)
// * 
// */
//package se.csn.common.shs;
//
//import se.statskontoret.shsapi.Label;
//import se.statskontoret.shsapi.SequenceType;
//import se.statskontoret.shsapi.ShsActor;
//import se.statskontoret.shsapi.Status;
//import se.statskontoret.shsapi.TransferType;
//
///**
// * Skapad 2007-feb-02
// * @author Jonas Öhrnell (csn7821)
// */
//public abstract class ShsUtils {
//
//    /**
//     * Skapar en SHS-Label. 
//     * @param from Från-adress
//     * @param to Till-adress
//     * @param commonName Namn i från-begreppet
//     * @param productType Produkttyp
//     * @param corrID Korrelationsid
//     * @param subject Meddelande-subject
//     */
//    public static Label createShsLabel(String from, String to, String commonName, String productType, String corrID, String subject) {
//		ShsActor actorFrom = new ShsActor(from);
//		actorFrom.setCommonName(commonName);
//	
//		Label label = new Label();	
//		label.setFrom(actorFrom);
//		label.setTo(to);
//		label.setProductType(productType);
//		label.setTransferType(TransferType.ASYNCH);
//		label.setSequenceType(SequenceType.EVENT);
//		label.setCorrId(corrID);
//		label.setMetaData("orgtyp", "11a");
//		label.setSubject(subject);
//		return label;
//	}
//    
//    /**
//     * @param status Antingen TEST eller PRODUCTION. Om värdet är PROD, PRODUCTION eller PRODUKTION
//     *        (case insensitive) så sätts status till PRODUCTION, annars sätts det till TEST. 
//     */
//    public static Label createShsLabel(String from, String to, String commonName, String productType, String corrID, String subject, String status) {
//		Label label = createShsLabel(from, to, commonName, productType, corrID, subject);
//		if(status.equalsIgnoreCase("PROD") || status.equalsIgnoreCase("PRODUKTION") || status.equalsIgnoreCase("PRODUCTION")) {
//		    label.setStatus(Status.PRODUCTION);
//		} else {
//		    label.setStatus(Status.TEST);
//		}
//		return label;
//	}
//
//}
