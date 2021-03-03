<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>

<petclinic:layout pageName="contrato">


<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fechaInicial").datepicker({dateFormat: 'yy/mm/dd'});
                $("#fechaFinal").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
</jsp:attribute>

<jsp:body>
<form:form modelAttribute="contrato" class="form-horizontal" id="add-contrato-form">
        <div class="form-group has-feedback">
        <fieldset></fieldset>
        <label>Veterinario:</label><select id="veterinario" name="veterinario">
               <option value="${veterinario.id}"><c:out value="${veterinario.firstName}"/>&nbsp;<c:out value="${veterinario.lastName}"/></option>
        </select><br></br>
        <label>Fecha petición:</label><c:out value="${contrato.fechaPeticion}"/><br></br>
            <label>Fecha inicial</label><petclinic:inputField label="${fechaInicial}" name="fechaInicial"/>
            <label>Fecha final</label><petclinic:inputField label="${fechaFinal}" name="fechaFinal"/>
            <label>Tipo de ganado</label>
            <select id="ganados" name="ganados" multiple required>
               <c:forEach items="${tiposG}" var="ganado">
               <option value="${ganado.id}"><c:out value="${ganado.tipoGanado}"/></option>
               </c:forEach>
            </select><br></br>
            <label>Explotación ganadera</label>
            <select id="explotacionGanadera" name="explotacionGanadera">
               <c:forEach items="${expGs}" var="explotacionGanadera">
               <option value="${explotacionGanadera.id}"><c:out value="${explotacionGanadera.name}"/></option>
               </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Enviar contrato</button>
            </div>
        </div>
    </form:form>
</jsp:body>
</petclinic:layout>