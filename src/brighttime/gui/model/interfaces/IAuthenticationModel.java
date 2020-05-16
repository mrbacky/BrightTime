/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.model.interfaces;

import brighttime.be.User;

/**
 *
 * @author rados
 */
public interface IAuthenticationModel {
 
    User authenticateUser(String username, String password);
}
