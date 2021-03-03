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
                $("#fechaIdentificacion").datepicker({dateFormat: 'yy/mm/dd'});
                $("#fechaNacimiento").datepicker({dateFormat: 'yy/mm/dd'});
                $("#fechaEntrada").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
        <script type="text/javascript">

       function mostrarAtributosComprado(){
        if (document.getElementById('comprado').checked == true) {
        document.getElementById('parteOculta').style.display='block';
        } else {
        document.getElementById('parteOculta').style.display='none';
      }
     }

</script>
    </jsp:attribute>

<jsp:body>
Nuevo animal de ganado <c:out value="${animal.tipoGanado.tipoGanado}"/>

<form:form modelAttribute="animal" class="form-horizontal" id="add-animal-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Identificador animal" name="identificadorAnimal"/>
            <label>Fecha identificación</label><petclinic:inputField label="${fechaIdentificacion}" name="fechaIdentificacion"/>
            <label>Fecha nacimiento</label><petclinic:inputField label="${fechaNacimiento}" name="fechaNacimiento"/>
            <label>Sexo</label>
            <select id="Sexo" name="sexo">
               <option value="Macho">Macho</option>
               <option value="Hembra">Hembra</option>
            </select>
            <br></br>
            <label>¿El animal ha sido comprado?</label><input <c:if test="${animal.comprado}">checked</c:if> type="checkbox" id="comprado" name="comprado" onclick="mostrarAtributosComprado()"/>
            <div id="parteOculta" style="display: <c:choose><c:when test="${animal.comprado}">block</c:when><c:otherwise>none</c:otherwise></c:choose>;">
            <petclinic:inputField label="Procedencia" name="procedencia"/>
            <label>Fecha entrada</label><petclinic:inputField label="${fechaEntrada}" name="fechaEntrada"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Crear animal</button>
            </div>
        </div>
    </form:form>
</jsp:body>
</petclinic:layout>