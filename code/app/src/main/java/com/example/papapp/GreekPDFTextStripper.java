//
//package com.example.papapp;
//import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.pdfbox.text.TextPosition;
//
//import java.io.IOException;
//import java.util.List;
//
//public class GreekPDFTextStripper extends PDFTextStripper {
//    private PDFont greekFont;
//
//    public GreekPDFTextStripper(PDFont greekFont) throws IOException {
//        this.greekFont = greekFont;
//    }
//
//    @Override
//    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
//        StringBuilder builder = new StringBuilder();
//        for (TextPosition textPosition : textPositions) {
//            if (isGreekCharacter(textPosition.getUnicode())) {
//                builder.append(textPosition.getUnicode());
//            }
//        }
//
//        // Perform your desired action with the Greek text
//        // For example, you can store it in a separate data structure or process it further
//
//        super.writeString(text, textPositions);
//    }
//
//    private boolean isGreekCharacter(String text) {
//        for (char c : text.toCharArray()) {
//            // Check if the character falls within the Greek Unicode range
//            if (c >= 0x0370 && c <= 0x03FF) {
//                return true; // Character is Greek
//            }
//        }
//        return false; // No Greek characters found
//    }
//
//}
//
//
