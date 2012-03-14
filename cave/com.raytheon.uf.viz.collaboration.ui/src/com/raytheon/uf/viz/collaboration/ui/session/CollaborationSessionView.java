package com.raytheon.uf.viz.collaboration.ui.session;

/**
 * This software was developed and / or modified by Raytheon Company,
 * pursuant to Contract DG133W-05-CQ-1067 with the US Government.
 * 
 * U.S. EXPORT CONTROLLED TECHNICAL DATA
 * This software product contains export-restricted data whose
 * export/transfer/disclosure is restricted by U.S. law. Dissemination
 * to non-U.S. persons whether in the United States or abroad requires
 * an export license or other authorization.
 * 
 * Contractor Name:        Raytheon Company
 * Contractor Address:     6825 Pine Street, Suite 340
 *                         Mail Stop B8
 *                         Omaha, NE 68106
 *                         402.291.0100
 * 
 * See the AWIPS II Master Rights File ("Master Rights File.pdf") for
 * further licensing information.
 **/

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.raytheon.uf.viz.collaboration.data.CollaborationDataManager;

/**
 * TODO Add Description
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Mar 1, 2012            rferrel     Initial creation
 * 
 * </pre>
 * 
 * @author rferrel
 * @version 1.0
 */
public class CollaborationSessionView extends SessionView {
    public static final String ID = "com.raytheon.uf.viz.collaboration.CollaborationSession";

    private static final String COLLABORATION_SESSION_IMAGE_NAME = "messages.gif";

    private Action switchToAction;

    protected void createActions() {
        super.createActions();
        switchToAction = new Action("Switch to...", Action.AS_DROP_DOWN_MENU) {
            public void run() {
                if ("DataProvider".equals(switchToAction.getId())) {
                    switchDataProvider();
                } else if ("SessionLeader".equals(switchToAction.getId())) {
                    switchLeader();
                }
            };
        };

        IMenuCreator creator = new IMenuCreator() {
            Menu menu;

            @Override
            public Menu getMenu(Menu parent) {
                if (menu == null) {
                    menu = new Menu(parent);
                }
                Action leaderAction = new Action("Session Leader") {
                    public void run() {
                        switchToAction.setId("SessionLeader");
                        switchToAction.run();
                    };
                };
                ActionContributionItem leaderItem = new ActionContributionItem(
                        leaderAction);
                leaderItem.fill(menu, -1);

                Action dataProviderAction = new Action("Data Provider") {
                    public void run() {
                        switchToAction.setId("DataProvider");
                        switchToAction.run();
                    };
                };
                ActionContributionItem dataProviderItem = new ActionContributionItem(
                        dataProviderAction);
                dataProviderItem.fill(menu, -1);
                return menu;
            }

            @Override
            public void dispose() {
                menu.dispose();
            }

            @Override
            public Menu getMenu(Control parent) {
                return getMenu(parent.getMenu());
            }
        };
        switchToAction.setMenuCreator(creator);
    }

    public void switchDataProvider() {
        System.out.println("Send switchDataProvider request.");
    }

    public void switchLeader() {
        System.out.println("Send switchLeader request");
    }

    @Override
    protected String getSessionImageName() {
        return COLLABORATION_SESSION_IMAGE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.uf.viz.collaboration.ui.session.SessionView#sendMessage()
     */
    @Override
    public void sendMessage() {
        String message = null;
        message = composeText.getText().trim();
        composeText.setText("");
        composeText.setCaretOffset(0);
        if (message.length() == 0) {
            // Do not send empty messages.
            return;
        }
        CollaborationDataManager.getInstance().getSession(sessionId)
                .sendMessageToVenue(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.uf.viz.collaboration.ui.session.SessionView#fillContextMenu
     * (org.eclipse.jface.action.IMenuManager)
     */
    @Override
    protected void fillContextMenu(IMenuManager manager) {
        super.fillContextMenu(manager);
        // check if data provider
        // check if session leader
        manager.add(switchToAction);
    }
}
