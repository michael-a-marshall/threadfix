<script type="text/ng-template" id="editCustomCweTextModal.html">
    <div class="modal-header">
        <h4 id="myModalLabel">
            Set Custom Text
            <span class="delete-span">
                <a id="deleteButton" class="btn btn-danger header-button" type="submit" ng-click="showDeleteDialog('Custom CWE Text')">Delete</a>
            </span>
        </h4>
    </div>

    <div ng-form="form" class="modal-body">
        <table class="modal-form-table">
            <tbody class="modal-form-table">
            <tr>
                <td>CWE</td>
                <td>
                    <input id="genericVulnerabilityName"
                           style="z-index:4000;width:500px"
                           type="text"
                           name="genericVulnerabilityName"
                           ng-model="object.genericVulnerability.name"
                           typeahead="genericVulnerability.name for genericVulnerability in config.genericVulnerabilities | filter:$viewValue | limitTo:10"
                           class="form-control">
                </td>
                <td>
                        <span id="genericVulnerabilityServerError" class="errors" ng-show="object.genericVulnerability_error">
                            {{ object.genericVulnerability_error }}
                        </span>
                </td>
            </tr>
            <tr>
                <td>Text</td>
                <td>
                    <textarea ng-model="object.customText" style="width:500px; height: 300px"></textarea>
                </td>
                <td>
                        <span id="customTextServerError" class="errors" ng-show="object.customText_error">
                            {{ object.customText_error }}
                        </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <%@ include file="/WEB-INF/views/modal/footer.jspf" %>
</script>