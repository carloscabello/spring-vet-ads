package org.springframework.samples.petclinic.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.samples.petclinic.model.TiposGanado;

public class TipoGanadoServiceTests {
	
	public static TiposGanado newTipoGanado(Integer id, String tipoGanado) {
		TiposGanado res = new TiposGanado();
		res.setTipoGanado(tipoGanado);
		res.setId(id);
		
		return res;
	}
	
	public static Map<String, TiposGanado> defaultTiposGanado(){

		Map<String, TiposGanado> res = new HashMap<String, TiposGanado>();
		
		TiposGanado porcino = newTipoGanado(1,"Porcino");
		res.put(porcino.getTipoGanado(), porcino);
		
		TiposGanado ovino = newTipoGanado(2,"Ovino");
		res.put(ovino.getTipoGanado(), ovino);
		
		TiposGanado vacuno = newTipoGanado(3,"Vacuno");
		res.put(vacuno.getTipoGanado(), vacuno);
		
		TiposGanado caprino = newTipoGanado(4,"Caprino");
		res.put(caprino.getTipoGanado(), caprino);
		
		TiposGanado equino = newTipoGanado(5,"Equino");
		res.put(equino.getTipoGanado(), equino);
		
		TiposGanado asnal = newTipoGanado(6,"Asnal");
		res.put(asnal.getTipoGanado(), asnal);
		
		TiposGanado avicola = newTipoGanado(7,"Avicola");
		res.put(avicola.getTipoGanado(), avicola);
		
		return res;
		
	}

}
