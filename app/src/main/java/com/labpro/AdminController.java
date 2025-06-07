//package com.labpro;
//
//public class AdminController {
//    private Object activeController;
//
//    public void switchController(Object newController) {
//        if (newController instanceof RepoKurirController ||
//                newController instanceof RepoParsElController ||
//                newController instanceof RepoPengirimanController) {
//
//            this.activeController = newController;
//            System.out.println("Controller telah berubah menjadi: " + newController.getClass().getSimpleName());
//        } else {
//            throw new IllegalArgumentException("Controller tidak valid untuk AdminController: " + newController.getClass().getSimpleName());
//        }
//    }
//
//    public Object getActiveController() {
//        return activeController;
//    }
//}