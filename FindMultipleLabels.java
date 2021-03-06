/*
 * Copyright (c) 2020 Abc Xyz — All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//Find all symbols with multiple labels. Maybe useful for Version Tracking session.
//@author saruman9
//@category Symbol
//@keybinding ctrl 1
//@menupath
//@toolbar

import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.Address;
import ghidra.program.model.symbol.Symbol;
import ghidra.program.model.symbol.SymbolIterator;
import ghidra.program.model.symbol.SymbolTable;
import ghidra.util.Msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindMultipleLabels extends GhidraScript {

    HashMap<Address, List<Symbol>> addresses = new HashMap<>();

    @Override
    protected void run() throws Exception {
        if (currentProgram == null) {
            Msg.showError(this,
                    null,
                    "Error",
                    "This script should be run from a tool with open program.");
            return;
        }
        SymbolTable symbolTable = currentProgram.getSymbolTable();
        SymbolIterator symbolIterator = symbolTable.getAllSymbols(true);
        while (symbolIterator.hasNext()) {
            Symbol symbol = symbolIterator.next();
            List<Symbol> currentSymbols =
                    addresses.getOrDefault(symbol.getAddress(), new ArrayList<>());
            currentSymbols.add(symbol);
            addresses.put(symbol.getAddress(), currentSymbols);
        }
        for (Map.Entry<Address, List<Symbol>> entry : addresses.entrySet()) {
            if (entry.getValue().size() > 1) {
                printf("%s - %s\n", entry.getKey(), entry.getValue());
            }
        }
    }
}
