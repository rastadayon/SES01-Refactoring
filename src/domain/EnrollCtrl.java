package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(EnrollRequest enrollRequest) throws EnrollmentRulesViolationException {
        enrollRequest.checkHasAlreadyPassedCourse();
        enrollRequest.checkHasRequestGPARequirements();
        enrollRequest.checkPrerequisites();
        enrollRequest.checkTimeConflicts();
        for (CSE o : enrollRequest.getCourses())
			enrollRequest.getStudent().takeCourse(o.getCourse(), o.getSection());
	}
}
