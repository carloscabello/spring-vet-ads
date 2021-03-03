<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="animal">

<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fechaFallecimiento").datepicker({dateFormat: 'yy/mm/dd'});
                $("#fechaVenta").datepicker({dateFormat: 'yy/mm/dd'});
            });
            function mostrarVenta() {
        		document.getElementById('venta').style.display="block";
        		document.getElementById('fallecimiento').style.display="none";
        		document.getElementById('add-animalHistorico-form').reset();
        	}
        	function mostrarFallecimiento() {
        		document.getElementById('venta').style.display="none";
        		document.getElementById('fallecimiento').style.display="block";
        		document.getElementById('add-animalHistorico-form').reset();
        	}
        </script>
</jsp:attribute>

<jsp:body>

<input type="button" id="fallecimientoBtn" name="fallecimientoBtn" value="Fallecimiento" onclick="mostrarFallecimiento();"/>
<input type="button" id="ventaBtn" name="ventaBtn" value="Venta" onclick="mostrarVenta();"/>
<br></br>

<form:form modelAttribute="animalHistorico" class="form-horizontal" id="add-animalHistorico-form">
        <div class="form-group has-feedback">
        
            <div id="fallecimiento">
            <label>Fecha fallecimiento</label><petclinic:inputField label="${fechaFallecimiento}" name="fechaFallecimiento"/>
            </div>
            <div id="venta" style="display:none;">
            <label>Fecha venta</label><petclinic:inputField label="${fechaVenta}" name="fechaVenta"/>
            </div>
            <petclinic:inputField label="Más información" name="masInfo"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Archivar animal</button>
            </div>
        </div>
    </form:form>
</jsp:body>
</petclinic:layout>