package src.main.java.main;

import src.main.java.gui.LiveSQLApp;


///NAPOMENA
/*
Podrzani upiti:

    select * from _______
    select ______ from ______
    select * from _______ order by asc ili desc
    select ______ from ______ order by ______ asc ili desc
    select * from ______ where _____ > ______ (select * from hr.employees where salary > 10000)
    select * from ______ where _____ < ______
    select * from ______ where _____ = ______
    select * from ______ where _____ like '______'
    select ______ from ______ where _____ > ______ (select last_name, first_name from hr.employees where salary > 10000)
    select ______ from ______ where _____ < ______
    select ______ from ______ where _____ = ______
    select ______ from ______ where _____ like '______'
    select ______ from ______ where _____ = _____ and ______ = ______ (select last_name, first_name from hr.employees where salary = 10000 and last_name like 'King')
    select ______ from ______ where _____ = _____ and ______ like ______
    select ______ from ______ where _____ = _____ or ______ = ______ (select last_name, first_name from hr.employees where salary = 10000 or last_name like 'King')
    select ______ from ______ where _____ = _____ or ______ like ______

    U select delu parametri se pisu za zarezom izmedju, nakon kojeg obavezno ide razmak
    select last_name, first_name - ovako treba
    select last_name,first_name - ovako ne

*/

public class Main {

    public static void main(String[] args) {

        LiveSQLApp mainFrame = LiveSQLApp.getInstance();
    }
}
