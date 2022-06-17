package domain;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(EnrollRequest enrollRequest) throws EnrollmentRulesViolationException {
        enrollRequest.checkHasAlreadyPassedCourse();
        enrollRequest.checkHasRequestGPARequirements();
        enrollRequest.checkPrerequisites();
        enrollRequest.checkTimeConflicts();
        for (CourseOffering courseOffering : enrollRequest.getCourseOfferings())
			enrollRequest.getStudent().takeCourse(courseOffering.getCourse(), courseOffering.getSection());
	}
}
