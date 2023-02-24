/*
 * Wilos Is a cLever process Orchestration Software - http://www.wilos-project.org
 * Copyright (C) 2006-2007 Paul Sabatier University, IUP ISI (Toulouse, France) <massie@irit.fr>
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package wilos.utils;

import java.text.SimpleDateFormat;

public class Constantes {

    public static final String DATE_PATTERN = "dd/MM/yyyy hh:mm";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
	    DATE_PATTERN);

    public static class State {

	public static final String CREATED = "Created";

	public static final String READY = "Ready";

	public static final String STARTED = "Started";

	public static final String SUSPENDED = "Suspended";

	public static final String FINISHED = "Finished";
    }

    public static final String[] WorkOrderTypes = {
	    WorkOrderType.START_TO_START, WorkOrderType.START_TO_FINISH,
	    WorkOrderType.FINISH_TO_START, WorkOrderType.FINISH_TO_FINISH };

    public static class WorkOrderType {

	public static final String START_TO_START = "startToStart";

	public static final String START_TO_FINISH = "startToFinish";

	public static final String FINISH_TO_START = "finishToStart";

	public static final String FINISH_TO_FINISH = "finishToFinish";
    }
}
