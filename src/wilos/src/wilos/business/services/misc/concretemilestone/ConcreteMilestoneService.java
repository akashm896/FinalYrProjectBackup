package wilos.business.services.misc.concretemilestone;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementService;
import wilos.business.services.misc.stateservice.StateService;
import wilos.hibernate.misc.concretemilestone.ConcreteMilestoneDao;
import wilos.model.misc.concretemilestone.ConcreteMilestone;
import wilos.utils.Constantes.State;

/**
 * ConcreteMilestoneService is a transactional class, that manages operations
 * about concrete milestones, requested by web pages
 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class ConcreteMilestoneService {

    private ConcreteMilestoneDao concreteMilestoneDao;

    private ConcreteWorkBreakdownElementService concreteWorkBreakdownElementService;

    private StateService stateService;

    /**
     * Allows to save the concreteMilestone
     * @param _concreteMilestone
     */
    public void saveConcreteMilestone(ConcreteMilestone _concreteMilestone) {
	this.concreteMilestoneDao
		.saveOrUpdateConcreteMilestone(_concreteMilestone);
    }

    /**
     * Allows to get the concreteMilestone with its id
     * @param _id
     * @return the concreteMilestone
     */
    public ConcreteMilestone getConcreteMilestone(String _id) {
	return this.concreteMilestoneDao.getConcreteMilestone(_id);
    }

    /**
     * Allows to get the concreteMilestoneDao
     * @return the concreteMilestoneDao
     */
    public ConcreteMilestoneDao getConcreteMilestoneDao() {
	return this.concreteMilestoneDao;
    }

    /**
     * Allows to set the concreteMilestoneDao
     * @param _concreteMilestoneDao
     *                
     */
    public void setConcreteMilestoneDao(
	    ConcreteMilestoneDao _concreteMilestoneDao) {
	this.concreteMilestoneDao = _concreteMilestoneDao;
    }

    /**
     * Allows to cross the concreteMilestone
     * @param _concreteMilestone
     */
    public void crossConcreteMilestone(ConcreteMilestone _concreteMilestone) {
	this.stateService.updateStateTo(_concreteMilestone, State.STARTED);
	this.stateService.updateStateTo(_concreteMilestone, State.FINISHED);
    }

    /**
     * Allows to check if a concreteMilestone exists with its id
     * @param _concreteMilestoneId
     * @return true if the concreteMilestone exists, false in the other case
     */
    public boolean existsConcreteMilestone(String _concreteMilestoneId) {
	return this.concreteMilestoneDao
		.existsConcreteMilestone(_concreteMilestoneId);
    }

    /**
     * Allows to get the concreteWorkBreakdownElementService
     * @return the concreteWorkBreakdownElementService
     */
    public ConcreteWorkBreakdownElementService getConcreteWorkBreakdownElementService() {
	return this.concreteWorkBreakdownElementService;
    }

    /**
     * Allows to set the concreteWorkBreakdownElementService
     * @param _concreteWorkBreakdownElementService
     *                
     */
    public void setConcreteWorkBreakdownElementService(
	    ConcreteWorkBreakdownElementService _concreteWorkBreakdownElementService) {
	this.concreteWorkBreakdownElementService = _concreteWorkBreakdownElementService;
    }

    /**
     * Allows to get the service's state
     * @return the stateService
     */
    public StateService getStateService() {
	return this.stateService;
    }

    /**
     * Allows to set the service's state
     * @param _stateService
     *                
     */
    public void setStateService(StateService _stateService) {
	this.stateService = _stateService;
    }
}
