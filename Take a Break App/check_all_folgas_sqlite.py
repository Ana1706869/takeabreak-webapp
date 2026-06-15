#!/usr/bin/env python3
import sqlite3

db_path = r'd:/Projeto de Informática/Take a Break App/data/AgendamentoFolgas.db'
con = sqlite3.connect(db_path)
cur = con.cursor()

print("=== All Folgas in SQLite by Funcionario ID ===")
result = cur.execute("""
SELECT f.FuncionarioId, func.Nome, COUNT(*) as count, GROUP_CONCAT(f.Estado, ', ') as estados
FROM Folgas f
LEFT JOIN [Funcionário] func ON f.FuncionarioId = func.FuncionarioId
GROUP BY f.FuncionarioId
ORDER BY f.FuncionarioId
""").fetchall()

for func_id, nome, count, estados in result:
    print(f"ID: {func_id}, Nome: {nome}, Total: {count}, Estados: {estados}")

print("\n=== Total Folgas ===")
total = cur.execute("SELECT COUNT(*) FROM Folgas").fetchone()[0]
print(f"Total folgas in SQLite: {total}")

con.close()
