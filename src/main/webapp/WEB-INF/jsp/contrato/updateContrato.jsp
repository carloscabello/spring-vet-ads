<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>

<petclinic:layout pageName="contratoAuxiliar">

<jsp:body>
<form:form modelAttribute="contratoAuxiliar" class="form-horizontal" id="add-contrato-form">
        <div class="form-group has-feedback">
        
            <label>Explotación ganadera:</label><c:out value="${contratoAuxiliar.explotacionGanadera.name}"/><br></br>
            <label>Veterinario:</label><c:out value="${contratoAuxiliar.veterinario.firstName}"/>&nbsp;<c:out value="${contratoAuxiliar.veterinario.lastName}"/><br></br>
            <br></br>
            <label>Fecha petición:</label><c:out value="${contratoAuxiliar.fechaPeticion}"/><br></br>
            <label>Fecha inicial:</label><c:out value="${contratoAuxiliar.fechaInicial}"/><br></br>
            <label>Fecha final:</label><c:out value="${contratoAuxiliar.fechaFinal}"/><br></br>
            <label>Ganados actuales del contrato:</label>
            <c:forEach items="${contratoAuxiliar.ganados}" var="ganados" varStatus="ganadosStatus">
        	    <c:out value="${ganados.tipoGanado}"/><c:if test="${!ganadosStatus.isLast()}">,&nbsp;</c:if>
        	</c:forEach><br></br>
            <label>Nuevos tipos de ganado a añadir al contrato:</label>
            <select id="ganados" name="ganados" multiple required>
               <c:forEach items="${nuevosGanadosContratoPosibles}" var="ganado">
               <option value="${ganado.id}"><c:out value="${ganado.tipoGanado}"/></option>
               </c:forEach>
            </select><br></br>
            <i>Los ganados que se pueden añadir son los ganados que puede llevar el veterinario contratado y que no se encuentran ya en el contrato</i>

        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Añadir ganados seleccionados</button>
            </div>
        </div>
    </form:form>
</jsp:body>
</petclinic:layout>