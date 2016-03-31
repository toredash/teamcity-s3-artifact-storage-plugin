<%@ page import="jetbrains.buildServer.artifacts.Constants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<h3>Some S3 settings</h3>
<table class="runnerFormTable">
    <l:settingsGroup title="S3 Parameters">
        <tr>
            <th><label for="storage.s3.keyId">S3 Access Key ID: </label></th>
            <td><props:textProperty name="<%=Constants.S3_KEY_ID%>" className="longField" maxlength="256"/>
                <span class="smallNote">S3 username</span>
            </td>
        </tr>
        <tr>
            <th><label for="storage.s3.secretkey">S3 Secret Access Key: </label></th>
            <td><props:textProperty name="<%=Constants.S3_SECRET_KEY%>"  className="longField buildTypeParams" maxlength="256"/>
                <span class="smallNote">S3 secret key</span>
            </td>
        </tr>
        <tr>
            <th><label for="storage.s3.bucket.name">S3 Bucket Name: </label></th>
            <td><props:textProperty name="<%=Constants.S3_BUCKET_NAME%>"  className="longField" maxlength="256"/>
                <span class="smallNote">S3 bucket name</span>
            </td>
        </tr>
        <tr>
            <th><label for="storage.s3.path.prefix">Path to build artifacts: </label></th>
            <td><props:textProperty name="<%=Constants.S3_PATH_PREFIX%>"  className="longField buildTypeParams" maxlength="256"/>
                <span class="smallNote">Path to build artifacts, e.g. storage/%teamcity.build.id%</span>
            </td>
        </tr>
    </l:settingsGroup>
</table>