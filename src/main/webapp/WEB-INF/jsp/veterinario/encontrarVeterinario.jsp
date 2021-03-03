<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Encontrar veterinario">

<form:form modelAttribute="tiposGanado" class="form-horizontal" id="search-veterinario-form">
        <div class="form-group has-feedback">
            <label>Tipo/s de ganado:</label>
            <select id="tiposGanado" name="tiposGanado" multiple>
               <c:forEach items="${todosTiposG}" var="ganado">
               <option value="${ganado.id}"><c:out value="${ganado.tipoGanado}"/></option>
               </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Buscar veterinario</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>