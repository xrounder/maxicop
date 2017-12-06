clear;
s1 = 'GCTTATA';
s2 = 'GCTATA';
s3 = 'GTTATA';
s4 = 'GCTTAGA';

scorematrix = eye(4);
scorematrix(scorematrix==0) = -1;

[score1,alignment1] = nwalign(s1,s2,'Alphabet','NT','ScoringMatrix',scorematrix,'GapOpen',2);
[score2,alignment2] = nwalign(s1,s3,'Alphabet','NT','ScoringMatrix',scorematrix,'GapOpen',2);
[score3,alignment3] = nwalign(s1,s4,'Alphabet','NT','ScoringMatrix',scorematrix,'GapOpen',2);
[score4,alignment4] = nwalign(s2,s3,'Alphabet','NT','ScoringMatrix',scorematrix,'GapOpen',2);
[score5,alignment5] = nwalign(s2,s4,'Alphabet','NT','ScoringMatrix',scorematrix,'GapOpen',2);
[score6,alignment6] = nwalign(s3,s4,'Alphabet','NT','ScoringMatrix',scorematrix,'GapOpen',2);

S = {s1,s2,s3,s4};
output = multialign(S,'ScoringMatrix',scorematrix,'GapOpen',2);
seqalignviewer(output)