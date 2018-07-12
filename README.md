# Server Emulator for Tekkit Classic

Current goals:
- load worlds from MC world files
- why the fuck does the notchian client try to read a String when we're sending Packet50s which doesn't even contain a byte[]?
- figure out why sending Packet3 is crashing the client (bad packet id, even though we're writing the id beforehand.)