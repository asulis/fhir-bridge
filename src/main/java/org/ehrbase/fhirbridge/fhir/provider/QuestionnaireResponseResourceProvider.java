package org.ehrbase.fhirbridge.fhir.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.ehrbase.fhirbridge.rest.EhrbaseService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.springframework.stereotype.Component;

@Component
public class QuestionnaireResponseResourceProvider extends AbstractResourceProvider {

    private final IFhirResourceDao<QuestionnaireResponse> questionnaireResponseDao;

    public QuestionnaireResponseResourceProvider(FhirContext context, EhrbaseService service, IFhirResourceDao<QuestionnaireResponse> questionnaireResponseDao) {
        super(context, service);
        this.questionnaireResponseDao = questionnaireResponseDao;
    }

    @Create
    @SuppressWarnings("unused")
    public MethodOutcome createQuestionnaireResponse(@ResourceParam QuestionnaireResponse questionnaireResponse) {
        questionnaireResponseDao.create(questionnaireResponse);
        return new MethodOutcome()
                .setCreated(true)
                .setResource(questionnaireResponse);
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return QuestionnaireResponse.class;
    }
}
