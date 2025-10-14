package se.csn.notmotor.ipl.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import se.csn.webservice.bas.notmotor.callback.DTOAvsandare;
import se.csn.webservice.bas.notmotor.callback.DTOBilaga;
import se.csn.webservice.bas.notmotor.callback.DTOMeddelande;
import se.csn.webservice.bas.notmotor.callback.DTOMeddelandeHandelse;
import se.csn.webservice.bas.notmotor.callback.DTOMottagare;

/**
 * Utilityklass for att konvertera notmotorns interna modelklasser till webbservice-dtoer och vice versa.
 * @author csn7823
 *
 */
public abstract class ConvertCallbackDTO {
	
	/**
	 * Konstruktor.
	 *
	 */
	private ConvertCallbackDTO() {	
	}
	
	/**
	 * 
	 * @param dto - webservice dto
	 * @return Meddelande - intern dto
	 */
	public static Meddelande getMeddelande(DTOMeddelande dto) {
		Meddelande nyDto = new Meddelande();
		nyDto.setId(dto.getId());
		nyDto.setCsnnummer(dto.getCsnnummer());
		nyDto.setRubrik(dto.getRubrik());
		nyDto.setMeddelandetext(dto.getMeddelandetext());
		nyDto.setRubrikEncoding(dto.getRubrikEncoding());
		nyDto.setMeddelandeEncoding(dto.getMeddelandeEncoding());
		
		if (dto.getBilagor() != null) {
			Bilaga[] bilagor = new Bilaga[dto.getBilagor().length];
			for (int i = 0; i < dto.getBilagor().length; i++) {
				bilagor[i] = new Bilaga();
				bilagor[i].setId(dto.getBilagor(i).getId());
				bilagor[i].setMimetyp(dto.getBilagor(i).getMimetyp());
				bilagor[i].setFilnamn(dto.getBilagor(i).getFilnamn());
				bilagor[i].setEncoding(dto.getBilagor(i).getEncoding());
	            
	            if (dto.getBilagor(i).getData() != null) {
	            	int langd = dto.getBilagor(i).getData().length;
	            	byte[] data = new byte[langd];
	            	System.arraycopy(dto.getBilagor(i).getData(), 0, data, 0, langd);
	            	bilagor[i].setData(data);
	            }
	        }
			nyDto.setBilagor(bilagor);
		}
		
		if (dto.getSkapad() != null) {
			nyDto.setSkapad(dto.getSkapad().getTime());
		}
		if (dto.getSkickat() != null) {
			nyDto.setSkickat(dto.getSkickat().getTime());
		}
		if (dto.getSkickaTidigast() != null) {
			nyDto.setSkickaTidigast(dto.getSkickaTidigast().getTime());
		}
		
		if (dto.getAvsandare() != null) {
        	Avsandare avs = new Avsandare();
        	avs.setApplikation(dto.getAvsandare().getApplikation());
        	avs.setKategori(dto.getAvsandare().getKategori());
        	avs.setNamn(dto.getAvsandare().getNamn());
        	avs.setEpostadress(dto.getAvsandare().getEpostadress());
        	avs.setReplyTo(dto.getAvsandare().getReplyTo());
        	nyDto.setAvsandare(avs);
        }
		
		if (dto.getMottagare() != null) {
			Mottagare[] mott = new Mottagare[dto.getMottagare().length];
			for (int i = 0; i < dto.getMottagare().length; i++) {
				mott[i] = new Mottagare();
				mott[i].setId(dto.getMottagare(i).getId());
				mott[i].setNamn(dto.getMottagare(i).getNamn());
				mott[i].setAdress(dto.getMottagare(i).getAdress());
				mott[i].setCsnnummer(dto.getMottagare(i).getCsnnummer());
				mott[i].setTyp(dto.getMottagare(i).getTyp());
				mott[i].setStatus(dto.getMottagare(i).getStatus());
	        }
			nyDto.setMottagare(mott);
		}
		
		if (dto.getHandelser() != null) {
			MeddelandeHandelse[] handelser = new MeddelandeHandelse[dto.getHandelser().length];
			for (int i = 0; i < dto.getHandelser().length; i++) {
				handelser[i] = new MeddelandeHandelse();
				handelser[i].setId(dto.getHandelser(i).getId());
				handelser[i].setHandelsetyp(dto.getHandelser(i).getHandelsetyp());
				if (dto.getHandelser(i).getTidpunkt() != null) {
					handelser[i].setTidpunkt(dto.getHandelser(i).getTidpunkt().getTime());
				}
				handelser[i].setFelkod(dto.getHandelser(i).getFelkod());
				handelser[i].setFeltext(dto.getHandelser(i).getFeltext());
				handelser[i].setInstans(dto.getHandelser(i).getInstans());
	        }
			nyDto.setHandelser(handelser);
		}
		
		nyDto.setCallbackURL(dto.getCallbackURL());
		nyDto.setCallbackMask(dto.getCallbackMask());
		nyDto.setMimetyp(dto.getMimetyp());
		nyDto.setKanal(dto.getKanal());
		
		return nyDto;
	}
	
	
	/**
	 * 
	 * @param dto - intern dto
	 * @return DTOMeddelande - webservice dto
	 */
	public static DTOMeddelande getMeddelande2(Meddelande dto) {
		DTOMeddelande nyDto = new DTOMeddelande();
		nyDto.setId(dto.getId());
		nyDto.setCsnnummer(dto.getCsnnummer());
		nyDto.setRubrik(dto.getRubrik());
		nyDto.setMeddelandetext(dto.getMeddelandetext());
		nyDto.setRubrikEncoding(dto.getRubrikEncoding());
		nyDto.setMeddelandeEncoding(dto.getMeddelandeEncoding());
		
		if (dto.getBilagor() != null) {
			DTOBilaga[] bilagor = new DTOBilaga[dto.getBilagor().length];
			for (int i = 0; i < dto.getBilagor().length; i++) {
				bilagor[i] = new DTOBilaga();
				bilagor[i].setId(dto.getBilaga(i).getId());
				bilagor[i].setMimetyp(dto.getBilaga(i).getMimetyp());
				bilagor[i].setFilnamn(dto.getBilaga(i).getFilnamn());
				bilagor[i].setEncoding(dto.getBilaga(i).getEncoding());
	            
	            if (dto.getBilaga(i).getData() != null) {
	            	int langd = dto.getBilaga(i).getData().length;
	            	byte[] data = new byte[langd];
	            	System.arraycopy(dto.getBilaga(i).getData(), 0, data, 0, langd);
	            	bilagor[i].setData(data);
	            }
	        }
			nyDto.setBilagor(bilagor);
		}
		
		if (dto.getSkapad() != null) {
			Calendar c = new GregorianCalendar();
			c.setTime(dto.getSkapad());
			nyDto.setSkapad(c);
		}
		if (dto.getSkickat() != null) {
			Calendar c = new GregorianCalendar();
			c.setTime(dto.getSkickat());
			nyDto.setSkickat(c);
		}
		if (dto.getSkickaTidigast() != null) {
			Calendar c = new GregorianCalendar();
			c.setTime(dto.getSkickaTidigast());
			nyDto.setSkickaTidigast(c);
		}
		
		if (dto.getAvsandare() != null) {
			DTOAvsandare avs = new DTOAvsandare();
        	avs.setApplikation(dto.getAvsandare().getApplikation());
        	avs.setKategori(dto.getAvsandare().getKategori());
        	avs.setNamn(dto.getAvsandare().getNamn());
        	avs.setEpostadress(dto.getAvsandare().getEpostadress());
        	avs.setReplyTo(dto.getAvsandare().getReplyTo());
        	nyDto.setAvsandare(avs);
        }
		
		if (dto.getMottagare() != null) {
			DTOMottagare[] mott = new DTOMottagare[dto.getMottagare().length];
			for (int i = 0; i < dto.getMottagare().length; i++) {
				mott[i] = new se.csn.webservice.bas.notmotor.callback.DTOMottagare();
				mott[i].setId(dto.getMottagare(i).getId());
				mott[i].setNamn(dto.getMottagare(i).getNamn());
				mott[i].setAdress(dto.getMottagare(i).getAdress());
				mott[i].setCsnnummer(dto.getMottagare(i).getCsnnummer());
				mott[i].setTyp(dto.getMottagare(i).getTyp());
				mott[i].setStatus(dto.getMottagare(i).getStatus());
	        }
			nyDto.setMottagare(mott);
		}
		
		if (dto.getHandelser() != null) {
			DTOMeddelandeHandelse[] handelser = new DTOMeddelandeHandelse[dto.getHandelser().length];
			for (int i = 0; i < dto.getHandelser().length; i++) {
				handelser[i] = new DTOMeddelandeHandelse();
				handelser[i].setId(dto.getHandelse(i).getId());
				handelser[i].setHandelsetyp(dto.getHandelse(i).getHandelsetyp());
				if (dto.getHandelse(i).getTidpunkt() != null) {
					Calendar c = new GregorianCalendar();
					c.setTime(dto.getHandelse(i).getTidpunkt());
					handelser[i].setTidpunkt(c);
				}
				handelser[i].setFelkod(dto.getHandelse(i).getFelkod());
				handelser[i].setFeltext(dto.getHandelse(i).getFeltext());
				handelser[i].setInstans(dto.getHandelse(i).getInstans());
	        }
			nyDto.setHandelser(handelser);
		}
		
		nyDto.setCallbackURL(dto.getCallbackURL());
		nyDto.setCallbackMask(dto.getCallbackMask());
		nyDto.setMimetyp(dto.getMimetyp());
		nyDto.setKanal(dto.getKanal());
		
		return nyDto;
	}
	
}
