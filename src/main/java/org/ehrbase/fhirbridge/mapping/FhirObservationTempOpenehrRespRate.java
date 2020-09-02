package org.ehrbase.fhirbridge.mapping;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.nedap.archie.rm.generic.PartySelf;
import org.ehrbase.fhirbridge.fhir.Profile;
import org.ehrbase.fhirbridge.opt.atemfrequenzcomposition.AtemfrequenzComposition;
import org.ehrbase.fhirbridge.opt.atemfrequenzcomposition.definition.AtemfrequenzObservation;
import org.ehrbase.fhirbridge.opt.intensivmedizinischesmonitoringkorpertemperaturcomposition.IntensivmedizinischesMonitoringKorpertemperaturComposition;
import org.ehrbase.fhirbridge.opt.intensivmedizinischesmonitoringkorpertemperaturcomposition.definition.KorpertemperaturBeliebigesEreignisChoice;
import org.ehrbase.fhirbridge.opt.intensivmedizinischesmonitoringkorpertemperaturcomposition.definition.KorpertemperaturBeliebigesEreignisPointEvent;
import org.ehrbase.fhirbridge.opt.intensivmedizinischesmonitoringkorpertemperaturcomposition.definition.KorpertemperaturObservation;
import org.ehrbase.fhirbridge.opt.shareddefinition.CategoryDefiningcode;
import org.ehrbase.fhirbridge.opt.shareddefinition.Language;
import org.ehrbase.fhirbridge.opt.shareddefinition.SettingDefiningcode;
import org.ehrbase.fhirbridge.opt.shareddefinition.Territory;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import static java.util.Date.from;

/**
 * FHIR 2 openEHR - respiration rate
 */
public class FhirObservationTempOpenehrRespRate {

    private static final Logger logger = LoggerFactory.getLogger(FhirObservationTempOpenehrRespRate.class);

    private FhirObservationTempOpenehrRespRate() {}

    public static AtemfrequenzComposition map(Observation fhirObservation) {

    	AtemfrequenzComposition composition = new AtemfrequenzComposition();

        // ========================================================================================
        // value quantity is expected
        Quantity fhirValue = null;
        BigDecimal fhirValueNumeric = null;
        DateTimeType fhirEffectiveDateTime = fhirObservation.getEffectiveDateTimeType();

        try {
            fhirValue = fhirObservation.getValueQuantity();
            fhirValueNumeric = fhirValue.getValue();
            logger.debug("Value numeric: {}", fhirValueNumeric);
        } catch (Exception e) {
            throw new UnprocessableEntityException(e.getMessage());
        }

        if (fhirValueNumeric == null) {
            throw new UnprocessableEntityException("Value is required in FHIR Observation and should be Quantity");
        }

        AtemfrequenzObservation tempObs = new AtemfrequenzObservation();
        tempObs.setTimeValue(fhirEffectiveDateTime.getValueAsCalendar().toZonedDateTime());
        tempObs.setOriginValue(fhirEffectiveDateTime.getValueAsCalendar().toZonedDateTime()); // mandatory
        tempObs.setLanguage(Language.EN); // FIXME: we need to grab the language from the template
        tempObs.setSubject(new PartySelf());

        List<AtemfrequenzObservation> observations = new ArrayList<>();
        tempObs.setMesswertMagnitude(fhirValueNumeric.doubleValue());
        tempObs.setMesswertUnits(fhirValue.getUnit());
        observations.add(tempObs);

        // ======================================================================================
        // Required fields by API
        composition.setLanguage(Language.EN); // FIXME: we need to grab the language from the template
        composition.setLocation("test");
        composition.setSettingDefiningcode(SettingDefiningcode.SECONDARY_MEDICAL_CARE);
        composition.setTerritory(Territory.DE);
        composition.setCategoryDefiningcode(CategoryDefiningcode.EVENT);
        composition.setStartTimeValue(fhirEffectiveDateTime.getValueAsCalendar().toZonedDateTime());
        composition.setComposer(new PartySelf());

        return composition;
    }
}