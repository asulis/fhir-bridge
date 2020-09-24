package org.ehrbase.fhirbridge.mapping.BlutGasAnalyse.LaboratoryAnalyteMappers;

import org.ehrbase.fhirbridge.opt.befundderblutgasanalysecomposition.definition.PhWertCluster;
import org.ehrbase.fhirbridge.opt.befundderblutgasanalysecomposition.definition.SauerstoffpartialdruckCluster;
import org.ehrbase.fhirbridge.opt.befundderblutgasanalysecomposition.definition.SauerstoffsattigungCluster;
import org.ehrbase.fhirbridge.opt.befundderblutgasanalysecomposition.definition.UntersuchterAnalytDefiningcode;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;

public class SauerstoffsaettigungMapper extends LaboratoryTestAnalyteMapper{
    public SauerstoffsaettigungMapper(Observation fhirObservation) {
        super(fhirObservation);
    }

    public SauerstoffsattigungCluster map() {
        SauerstoffsattigungCluster sauerstoffsattigungCluster = new SauerstoffsattigungCluster();
        sauerstoffsattigungCluster.setErgebnisStatusValue(mapErgebnisStatus());
        sauerstoffsattigungCluster.setUntersuchterAnalytDefiningcode(mapUntersuchterAnalyt());
        sauerstoffsattigungCluster.setAnalytResultatUnits("%");
        //TODO this profile also has normal values ? but no text or descriptions for them
        sauerstoffsattigungCluster.setErgebnisStatusValue(mapValue());
        return sauerstoffsattigungCluster;
    }

    @Override
    UntersuchterAnalytDefiningcode mapUntersuchterAnalyt() {
        UntersuchterAnalytDefiningcode oxygenSaturationInBlood = UntersuchterAnalytDefiningcode.OXYGEN_SATURATION_IN_BLOOD;
        UntersuchterAnalytDefiningcode oxygenSaturationInArterialBlood= UntersuchterAnalytDefiningcode.OXYGEN_SATURATION_IN_ARTERIAL_BLOOD;

        for (Coding coding : fhirObservation.getCode().getCoding()) {
            String code = coding.getCode();
            if (code.equals(oxygenSaturationInBlood.getCode())) {
                return oxygenSaturationInBlood;
            } else if (code.equals(oxygenSaturationInArterialBlood.getCode())) {
                return oxygenSaturationInArterialBlood;
            }
        }
        throw new IllegalArgumentException("The coding of the Untersuchter Analyte cannot be mapped, since code " + fhirObservation.getCode().getCoding() + " is unknown");
    }

}
